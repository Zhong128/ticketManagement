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
//    @Autowired
//    private DomeInterceptor domeInterceptor;
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
                        "/api/home/**",                   // 首页相关
                        "/api/events/**",                 // 演出列表
                        "/api/categories/**",             // 分类查询
                        "/api/cities/**",                 // 城市查询
                        "/api/captcha/**",                // 图形验证码接口
                        "/error"                          // 错误页面
                );





    }
}
