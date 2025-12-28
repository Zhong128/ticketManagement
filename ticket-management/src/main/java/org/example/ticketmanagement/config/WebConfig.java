package org.example.ticketmanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.intercepter.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/auth/user/login",           // 用户登录
                        "/api/auth/user/register",        // 用户注册
                        "/api/auth/admin/login",          // 管理员登录
                        "/api/auth/wechat/**",            // 微信相关接口
                        "/api/public/**",                 // 公共接口
                        "/api/home/recommend/default",    // 默认首页推荐
                        "/api/events",                    // 演出列表（公共）
                        "/api/events/search/**",          // 演出搜索（公共）
                        "/api/categories",                // 分类列表（公共）
                        "/api/categories/search/**",      // 分类搜索（公共）
                        "/api/cities",                    // 城市列表（公共）
                        "/api/cities/search/**",          // 城市搜索（公共）
                        "/api/sessions",                  // 场次列表（公共）
                        "/api/sessions/upcoming",         // 即将开始场次（公共）
                        "/api/ticket-tiers",              // 票档列表（公共）
                        "/api/ticket-tiers/price-range",  // 价格范围查询（公共）
                        "/api/captcha/**",                // 图形验证码接口
                        "/error"                          // 错误页面
                );
    }

}
