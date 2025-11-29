package org.example.ticketmanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
@ConfigurationProperties(prefix = "wechat.test")
public class WechatConfig {
    private String appId;
    private String appSecret;
    private String redirectUri;

}
