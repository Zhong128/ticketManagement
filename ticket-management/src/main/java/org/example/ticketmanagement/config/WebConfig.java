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
                .excludePathPatterns("/login", "/logout","/register",
                                    "/api/auth/wechat/qrcode",
                                    "/api/auth/wechat/callback");




    }
}
