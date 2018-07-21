package com.zoctan.seedling.core.jwt;

import com.zoctan.seedling.core.rsa.RsaUtils;
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
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Json web token 工具
 * 验证、生成 token
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
    private RsaUtils rsaUtils;
    @Resource
    private JwtConfigurationProperties jwtProperties;

    private Claims getClaims(final String token) {
        final Jws<Claims> jws = this.parseToken(token);
        return jws == null ? null : jws.getBody();
    }

    /**
     * 根据 token 得到账户名
     */
    public String getName(final String token) {
        final Claims claims = this.getClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * 签发 token
     *
     * @param name               账户名
     * @param grantedAuthorities 账户权限信息[ADMIN, TEST, ...]
     */
    public String sign(final String name, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 函数式创建 token，避免重复书写
        final Supplier<String> createToken = () -> this.createToken(name, grantedAuthorities);
        // 看看缓存有没有账户token
        final String token = (String) this.redisUtils.getValue(name);
        // 没有登录过
        if (token == null) {
            return createToken.get();
        }
        final Boolean isValidate = (boolean) this.redisUtils.getValue(token);
        // 有 token，仍有效，将 token 置为无效，并重新签发（防止 token 被利用）
        if (isValidate) {
            this.invalidRedisToken(name);
        }
        // 重新签发
        return createToken.get();
    }

    /**
     * 清除账户在 Redis 中缓存的 token
     *
     * @param name 账户名
     */
    public void invalidRedisToken(final String name) {
        // 将 token 设置为无效
        final String token = (String) this.redisUtils.getValue(name);
        this.redisUtils.setValue(token, false);
    }

    /**
     * 从请求头或请求参数中获取 token
     */
    public String getTokenFromRequest(final HttpServletRequest httpRequest) {
        final String header = this.jwtProperties.getHeader();
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
        // 解析 token 的 payload
        final Claims claims = this.getClaims(token);
        // 因为 JwtAuthenticationFilter 拦截器已经检查过 token 有效，所以可以忽略 get 空指针提示
        assert claims != null;
        // 获取账户角色字符串
        // 将元素转换为 GrantedAuthority 接口集合
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(this.jwtProperties.getClaimKeyAuth()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        final User principal = new User(name, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * 验证 token 是否正确
     */
    public boolean validateToken(final String token) {
        boolean isValidate = true;
        try {
            isValidate = (boolean) this.redisUtils.getValue(token);
        } catch (final NullPointerException e) {
            // 可能 redis 部署出现了问题
            // 或者清空了缓存导致 token 键不存在
            log.error(e.getMessage());
        }
        // 能正确解析 token，并且 redis 中缓存的 token 也是有效的
        return this.parseToken(token) != null && isValidate;
    }

    /**
     * 生成 token
     */
    private String createToken(final String name, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 获取账户的角色字符串，如 USER,ADMIN
        final String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.debug("==> Account<{}> authorities: {}", name, authorities);

        // 过期时间
        final long expireTime = this.jwtProperties.getExpireTime();
        // 当前时间 + 有效时长
        final Date expireDate = new Date(System.currentTimeMillis() + Duration.ofMinutes(expireTime).toMillis());
        // 创建 token，比如 "Bearer abc1234"
        final String token = this.jwtProperties.getTokenType() + " " +
                Jwts.builder()
                        // 设置账户名
                        .setSubject(name)
                        // 添加权限属性
                        .claim(this.jwtProperties.getClaimKeyAuth(), authorities)
                        // 设置失效时间
                        .setExpiration(expireDate)
                        // 私钥加密生成签名
                        .signWith(SignatureAlgorithm.RS256, this.rsaUtils.loadPrivateKey())
                        // 使用LZ77算法与哈夫曼编码结合的压缩算法进行压缩
                        .compressWith(CompressionCodecs.DEFLATE)
                        .compact();
        // 保存账户 token
        // 因为账户注销后 JWT 本身只要没过期就仍然有效，所以只能通过 redis 缓存来校验有无效
        // 校验时只要 redis 中的 token 无效即可（JWT 本身可以校验有无过期，而 redis 过期即被删除了）
        // true 有效
        this.redisUtils.setValue(token, true, expireTime);
        // redis 过期时间和JWT的一致
        this.redisUtils.setValue(name, token, expireTime);
        log.debug("==> Redis set Account<{}> token: {}", name, token);
        return token;
    }

    /**
     * 解析 token
     */
    private Jws<Claims> parseToken(final String token) {
        try {
            return Jwts
                    .parser()
                    // 公钥解密
                    .setSigningKey(this.rsaUtils.loadPublicKey())
                    .parseClaimsJws(token.replace(this.jwtProperties.getTokenType(), ""));
        } catch (final SignatureException e) {
            // 签名异常
            log.debug("Invalid JWT signature");
        } catch (final MalformedJwtException e) {
            // 格式错误
            log.debug("Invalid JWT token");
        } catch (final ExpiredJwtException e) {
            // 过期
            log.debug("Expired JWT token");
        } catch (final UnsupportedJwtException e) {
            // 不支持该JWT
            log.debug("Unsupported JWT token");
        } catch (final IllegalArgumentException e) {
            // 参数错误异常
            log.debug("JWT token compact of handler are invalid");
        }
        return null;
    }
}