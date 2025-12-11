package org.example.ticketmanagement.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1．获取到请求路径
        String requestURI = request.getRequestURI();

//        //原版代码，下面是AI优化后的精确匹配代码
////        if (requestURI.contains("/login")){
////            log.info("登录操作，放行");
////            return true;
////        }
//        //判断是否是登录请求，如果路径中包含/login，说明是登录操作，放行
//        if ("/login".equals(requestURI)) {
//            log.info("登录操作，放行");
//            return true;
//        }
//        //获取请求头中的token
//        String token = request.getHeader("token");

        // 精确匹配登录和注册路径
        // TODO：webConfig不是已经排除路径了嘛
        if ("/login".equals(requestURI) || "/register".equals(requestURI)) {
            log.info("登录或注册操作，放行");
            return true;
        }

        // 从请求头获取token（建议使用标准的Authorization头）
        String token = request.getHeader("Authorization");

        // 如果没有Authorization头，尝试获取token头（兼容现有实现）
        if (token == null || token.isEmpty()) {
            token = request.getHeader("token");
        } else if (token.startsWith("Bearer ")) {
            // 如果是Bearer格式，提取实际token
            token = token.substring(7);
        }

        //4，判断token是否存在，如果不存在，说明用户没有登录，返回错误信息（响应401状态码）
        if (token == null || token.isEmpty()){
            log.info("没有令牌，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return  false;

        }

        //5，如果token存在，校验令牌，如果校验失败->返回错误信息（响应401状态码）
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌校验失败，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return  false;
        }

        //6．校验通过，放行
        log.info("令牌校验通过，放行");
        return true;

        // TODO：你的token没有存点啥嘛？
    }
}
