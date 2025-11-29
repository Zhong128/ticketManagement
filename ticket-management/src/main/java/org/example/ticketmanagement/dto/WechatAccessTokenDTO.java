package org.example.ticketmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 微信access_token响应DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatAccessTokenDTO {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
