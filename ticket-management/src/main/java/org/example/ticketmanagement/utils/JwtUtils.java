package org.example.ticketmanagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT令牌工具类
 * 用于生成和解析JWT令牌
 */
public class JwtUtils {

    /**
     * 自动生成的加密密钥，符合HMAC-SHA256算法的安全要求
     */
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 令牌过期时间（12小时）
     */
    private static final long EXPIRATION_TIME = 12 * 3600 * 1000; // 12小时(实际上线时在没有加安全参数时可以改为30分钟提高安全性)

    /**
     * 生成JWT令牌
     *
     * @param claims 自定义声明信息
     * @return JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 使用 SecretKey 签名
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌字符串
     * @return 包含声明信息的Claims对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 使用 SecretKey 验证签名
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
