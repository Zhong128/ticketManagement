// org/example/ticketmanagement/util/JwtUtils.java
package org.example.ticketmanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
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
     * 生成JWT令牌(模板方法)
     *
     * @param userId 用户ID
     * @return JWT令牌字符串
     */
    public static String createUserClaims(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role != null ? role : "USER");

        return Jwts.builder()
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }


    /**
     * 生成JWT令牌(通用方法)
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

    /**
     * 从token中提取用户ID
     *
     * @param token JWT令牌字符串
     * @return 用户ID，如果解析失败返回null
     */
    public static Long getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj != null) {
                return Long.parseLong(userIdObj.toString());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否有效且包含用户ID
     *
     * @param token JWT令牌字符串
     * @return 是否有效
     */
    public static boolean isValidToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("userId") != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从token中提取用户角色
     */
    public static String getUserRoleFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object roleObj = claims.get("role");
            return roleObj != null ? roleObj.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

}
