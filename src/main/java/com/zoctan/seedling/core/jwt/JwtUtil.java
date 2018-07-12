package com.zoctan.seedling.core.jwt;

import com.zoctan.seedling.util.RSAUtils;
import com.zoctan.seedling.util.RedisUtils;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Json web token 工具
 * 验证、生成token
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class JwtUtil {
    private final static Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RSAUtils rsaUtils;
    @Resource
    private JWTSetting jwtSetting;

    private Claims getClaims(final String token) {
        final Jws<Claims> jws = this.parseToken(token);
        return jws == null ? null : jws.getBody();
    }

    /**
     * 根据token得到账户名
     */
    public String getName(final String token) {
        final Claims claims = this.getClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * 签发token
     *
     * @param name               账户名
     * @param grantedAuthorities 账户权限信息[ADMIN, TEST, ...]
     */
    public String sign(final String name, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 函数式创建token，避免重复书写
        final Supplier<String> createToken = () -> this.createToken(name, grantedAuthorities);
        // 看看缓存有没有账户token
        final String token = (String) this.redisUtils.get(name);
        // 没有登录过
        if (token == null) {
            return createToken.get();
        }
        final Boolean isValidate = (boolean) this.redisUtils.get(token);
        // 有token，仍有效，将token置为无效，并重新签发（防止token被利用）
        if (isValidate) {
            this.invalidRedisToken(name);
        }
        // 重新签发
        return createToken.get();
    }

    /**
     * 清除账户在Redis中缓存的token
     *
     * @param name 账户名
     */
    public void invalidRedisToken(final String name) {
        // 将token设置为无效
        final String token = (String) this.redisUtils.get(name);
        this.redisUtils.set(token, false);
    }

    /**
     * 从请求头或请求参数中获取token
     */
    public String getTokenFromRequest(final HttpServletRequest httpRequest) {
        final String header = this.jwtSetting.getHeader();
        final String token = httpRequest.getHeader(header);
        if (StringUtils.isEmpty(token)) {
            return httpRequest.getParameter(header);
        }
        return token;
    }

    /**
     * 返回账户认证
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String name, final String token) {
        // 解析token的payload
        final Claims claims = this.getClaims(token);
        // 获取账户角色字符串
        // 将元素转换为GrantedAuthority接口集合
        final Collection<? extends GrantedAuthority> authorities =
                // 因为JwtAuthenticationFilter拦截器已经检查过token有效，所以可以忽略get空指针提示
                Arrays.stream(claims.get(this.jwtSetting.getAuthoritiesKey()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        final User principal = new User(name, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * 验证token是否正确
     */
    public boolean validateToken(final String token) {
        boolean isValidate = true;
        try {
            isValidate = (boolean) this.redisUtils.get(token);
        } catch (final NullPointerException e) {
            // 可能redis部署出现了问题，或者清空了缓存导致token键不存在
            log.error(e.getMessage());
        }
        // 能正确解析token，并且redis中缓存的token也是有效的
        return this.parseToken(token) != null && isValidate;
    }

    /**
     * 生成token
     */
    private String createToken(final String name, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 获取账户的角色字符串，如 USER,ADMIN
        final String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.debug("Account<{}> : authorities => {}", name, authorities);
        
        // 过期时间
        final long expirationTime = this.jwtSetting.getExpirationTime();
        // 当前时间 + 有效时长
        final Date expiration = new Date(System.currentTimeMillis() + Duration.ofMinutes(expirationTime).toMillis());
        // 加载私钥
        final PrivateKey privateKey = this.rsaUtils.loadPemPrivateKey(this.jwtSetting.getPrivateKey());
        // 创建token
        final String token = this.jwtSetting.getTokenPrefix() + " " +
                Jwts.builder()
                        // 设置账户名
                        .setSubject(name)
                        // 添加权限属性
                        .claim(this.jwtSetting.getAuthoritiesKey(), authorities)
                        // 设置失效时间
                        .setExpiration(expiration)
                        // 私钥加密生成签名
                        .signWith(SignatureAlgorithm.RS256, privateKey)
                        // 使用LZ77算法与哈夫曼编码结合的压缩算法进行压缩
                        // 如果希望claim中的属性能被前端读取，建议不压缩
                        .compressWith(CompressionCodecs.DEFLATE)
                        .compact();
        // 保存账户token
        // 因为账户注销后JWT本身只要没过期就仍然有效，所以只能通过redis缓存来校验有无效
        // 校验时只要redis中的token无效即可（JWT本身可以校验有无过期，而redis过期即被删除了）
        // true 有效
        this.redisUtils.set(token, true, expirationTime);
        // redis过期时间和JWT的一致
        this.redisUtils.set(name, token, expirationTime);
        log.debug("redis => set account<{}> token : {}", name, token);
        return token;
    }

    /**
     * 解析token
     */
    private Jws<Claims> parseToken(final String token) {
        try {
            // 加载公钥
            final PublicKey publicKey = this.rsaUtils.loadPemPublicKey(this.jwtSetting.getPublicKey());
            return Jwts
                    .parser()
                    // 公钥解密
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token.replace(this.jwtSetting.getTokenPrefix(), ""));
        } catch (final SignatureException e) {
            // 签名异常
            log.error("Invalid JWT signature");
        } catch (final MalformedJwtException e) {
            // JWT格式错误
            log.error("Invalid JWT token");
        } catch (final ExpiredJwtException e) {
            // JWT过期
            log.error("Expired JWT token");
        } catch (final UnsupportedJwtException e) {
            // 不支持该JWT
            log.error("Unsupported JWT token");
        } catch (final IllegalArgumentException e) {
            // 参数错误异常
            log.error("JWT token compact of handler are invalid");
        }
        return null;
    }
}