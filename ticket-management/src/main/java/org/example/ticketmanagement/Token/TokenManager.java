package org.example.ticketmanagement.Token;

import io.jsonwebtoken.Claims;
import org.example.ticketmanagement.util.JwtUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Component
public class TokenManager {
    // 使用线程安全的集合存储黑名单token
    private Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * 将token加入黑名单
     */
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
        // 清理已过期的token
        cleanupExpiredTokens();
    }

    /**
     * 检查token是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        if (token == null) {
            return true;
        }

        if (!blacklistedTokens.contains(token)) {
            return false;
        }

        // 验证token是否仍然有效
        try {
            Claims claims = JwtUtils.parseToken(token);
            // 如果token已过期，则从黑名单中移除
            if (claims.getExpiration().before(new java.util.Date())) {
                blacklistedTokens.remove(token);
                return false;
            }
            return true;
        } catch (Exception e) {
            // token无效则从黑名单移除
            blacklistedTokens.remove(token);
            return false;
        }
    }

    /**
     * 清理已过期的token
     */
    private void cleanupExpiredTokens() {
        blacklistedTokens.removeIf(token -> {
            try {
                Claims claims = JwtUtils.parseToken(token);
                return claims.getExpiration().before(new java.util.Date());
            } catch (Exception e) {
                return true; // 无效token直接移除
            }
        });
    }
}
