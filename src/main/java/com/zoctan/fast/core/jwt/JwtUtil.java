package com.zoctan.fast.core.jwt;

import com.zoctan.fast.core.redis.RedisUtil;
import com.zoctan.fast.util.RSAUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtUtil {
    @Value("${jwt.authorities-key}")
    private String AUTHORITIES_KEY;
    // RSA私钥
    @Value("${jwt.private-key}")
    private String PRIVATE_KEY;
    // RSA公钥
    @Value("${jwt.public-key}")
    private String PUBLIC_KEY;
    // token前缀
    @Value("${jwt.token-prefix}")
    private String TOKEN_PREFIX;
    // 存放token的Header Key
    @Value("${jwt.header}")
    private String HEADER;
    // 有效期
    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RSAUtil rsaUtil;

    private Claims getClaims(final String token) {
        final Jws<Claims> jws = this.parseToken(token);
        return jws == null ? null : jws.getBody();
    }

    /**
     * 根据token得到用户名
     */
    public String getUsername(final String token) {
        final Claims claims = this.getClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * 创建token
     */
    public String sign(final String username, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 看看缓存有没有用户token，如果用户登陆过，发回该token
        String token = (String) this.redisUtil.get(username);
        if (token == null) {
            // 获取用户的角色字符串，如 USER,ADMIN
            final String authorities = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

            log.info("User<{}> : authorities => {}", username, authorities);

            token = this.createToken(username, authorities);

        }
        return token;
    }

    /**
     * 从请求头或请求参数中获取token
     */
    public String getTokenFromRequest(final HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(this.HEADER);
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(this.HEADER);
        }
        return token;
    }

    /**
     * 返回用户认证
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String username, final String token) {
        // 验证成功，重置token的过期时间
        // jwt过期时间已经定下了，除非重新下发token，不然redis单方面重置没用
        // 这里就不下发了，过期再登录
        //this.redisUtil.setExpire(username, EXPIRATION_TIME);
        //log.info("redis => update user<{}> token expiration time : {}", username, token);

        // 尝试从缓存得到授权信息
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) this.redisUtil.get(username + this.HEADER);
        log.info("redis => get user<{}> AuthenticationToken : {}", username, authenticationToken);
        if (authenticationToken == null) {
            // 解析token的payload
            final Claims claims = this.getClaims(token);

            // 获取用户角色字符串
            // 将元素转换为GrantedAuthority接口集合
            final Collection<? extends GrantedAuthority> authorities =
                    // 因为JwtAuthenticationFilter拦截器已经检查过token有效，所以可以忽略get空指针提示
                    Arrays.stream(claims.get(this.AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            final User principal = new User(username, "", authorities);
            authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);

            this.redisUtil.set(username + this.HEADER, authenticationToken, this.EXPIRATION_TIME);
            log.info("redis => first set user<{}> AuthenticationToken : {}", username, authenticationToken);
        }
        return authenticationToken;
    }

    /**
     * 验证token是否正确
     */
    public boolean validateToken(final String token) {
        return this.parseToken(token) != null;
    }

    /**
     * 生成token
     */
    private String createToken(final String username, final String authorities) {
        final Date date = new Date(System.currentTimeMillis() + this.EXPIRATION_TIME * 1000);
        // 加载私钥
        final PrivateKey privateKey = this.rsaUtil.loadPemPrivateKey(this.PRIVATE_KEY);
        // 创建token
        final String token = this.TOKEN_PREFIX + " " +
                Jwts.builder()
                        // 设置用户名
                        .setSubject(username)
                        // 添加权限属性
                        .claim(this.AUTHORITIES_KEY, authorities)
                        // 设置失效时间
                        .setExpiration(date)
                        // 私钥加密生成签名
                        // 用的是512位的私钥，RS512提醒key太短……这里有点奇怪
                        .signWith(SignatureAlgorithm.RS256, privateKey)
                        .compact();
        // 保存用户token
        this.redisUtil.set(username, token, this.EXPIRATION_TIME);
        log.info("redis => put user<{}> token : {}", username, token);
        return token;
    }

    /**
     * 解析token
     */
    private Jws<Claims> parseToken(final String token) {
        try {
            // 加载公钥
            final PublicKey publicKey = this.rsaUtil.loadPemPublicKey(this.PUBLIC_KEY);
            return Jwts
                    .parser()
                    // 公钥解密
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token.replace(this.TOKEN_PREFIX, ""));
        } catch (final SignatureException e) {
            // 签名异常
            log.info("Invalid JWT signature");
        } catch (final MalformedJwtException e) {
            // JWT格式错误
            log.info("Invalid JWT token");
        } catch (final ExpiredJwtException e) {
            // JWT过期
            log.info("Expired JWT token");
        } catch (final UnsupportedJwtException e) {
            // 不支持该JWT
            log.info("Unsupported JWT token");
        } catch (final IllegalArgumentException e) {
            // 参数错误异常
            log.info("JWT token compact of handler are invalid");
        }
        return null;
    }
}