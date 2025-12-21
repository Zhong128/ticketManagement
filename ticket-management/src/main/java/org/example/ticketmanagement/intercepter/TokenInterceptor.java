// TokenInterceptor.java 修改
package org.example.ticketmanagement.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.Token.TokenManager;
import org.example.ticketmanagement.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求路径
        String requestURI = request.getRequestURI();
        log.debug("拦截器处理请求: {}", requestURI);

        // 2. 从请求头获取token
        String token = extractTokenFromRequest(request);

        // 白名单检查
        if (!requiresAuthentication(requestURI)) {
            return true;
        }

        // 3. 判断token是否存在
        if (token == null || token.isEmpty()) {
            log.info("请求 {} 没有令牌", requestURI);
            // 对于需要登录的接口，返回401
            if (requiresAuthentication(requestURI)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            // 对于不需要登录的接口，直接放行
            return true;
        }

        // 4. 检查token是否在黑名单中
        if (tokenManager.isTokenBlacklisted(token)) {
            log.info("令牌已在黑名单中，请求: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 5. 校验令牌并提取用户ID和角色
        Long userId;
        String role;
        try {
            userId = JwtUtils.getUserIdFromToken(token);
            role = JwtUtils.getUserRoleFromToken(token);
            if (userId == null) {
                throw new Exception("令牌中没有用户ID");
            }
        } catch (Exception e) {
            log.info("令牌校验失败: {}, 错误: {}", requestURI, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 检查角色权限
        if (!hasPermission(requestURI, role)) {
            log.info("用户 {} (角色: {}) 无权访问: {}", userId, role, requestURI);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // 6. 将用户ID存储到请求属性中，供后续使用
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);
        log.debug("用户 {} (角色: {}) 的令牌校验通过，放行请求: {}", userId, role, requestURI);
        return true;
    }

    /**
     * 检查用户是否有权限访问该路径
     */
    private boolean hasPermission(String requestURI, String role) {
        // 管理端接口
        if (requestURI.startsWith("/api/admin/")) {
            return "ADMIN".equals(role);
        }
        // 用户端接口
        else if (requestURI.startsWith("/api/user/")) {
            return "USER".equals(role) || "ADMIN".equals(role); // ADMIN也可以访问用户接口
        }
        // 公共接口
        return true;
    }

    /**
     * 从请求中提取token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // 优先从Authorization头获取（标准方式）
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 兼容现有实现：从token头获取
        String tokenHeader = request.getHeader("token");
        if (tokenHeader != null && !tokenHeader.isEmpty()) {
            return tokenHeader;
        }

        // 从请求参数中获取（可选）
        String tokenParam = request.getParameter("token");
        if (tokenParam != null && !tokenParam.isEmpty()) {
            return tokenParam;
        }

        return null;
    }

    /**
     * 判断请求路径是否需要认证
     */
    private boolean requiresAuthentication(String requestURI) {
        // 不需要认证的白名单
        String[] whiteList = {
                "/api/auth/user/login",           // 用户登录
                "/api/auth/user/register",        // 用户注册
                "/api/auth/admin/login",          // 管理员登录
                "/api/auth/wechat/qrcode",        // 微信二维码
                "/api/auth/wechat/callback",      // 微信回调
                "/api/home/recommend/default",    // 默认首页推荐
                "/api/home/recommend/city/",      // 指定城市推荐
                "/api/events",                    // 演出列表查询
                "/api/categories",                // 分类查询
                "/api/cities",                    // 城市查询
                "/api/public/**",                 // 所有公共接口
                "/error"                          // 错误页面
        };

        for (String whitePath : whiteList) {
            if (requestURI.equals(whitePath) || requestURI.startsWith(whitePath)) {
                return false;
            }
        }

        // 其他接口需要认证
        return true;
    }
}