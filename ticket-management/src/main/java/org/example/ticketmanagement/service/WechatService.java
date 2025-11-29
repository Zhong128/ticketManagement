package org.example.ticketmanagement.service;

import org.example.ticketmanagement.dto.WechatLoginResultDTO;

public interface WechatService {
    /**
     * 获取微信登录二维码
     */
    String getLoginQrCode();
    /**
     * 处理微信回调
    */
    WechatLoginResultDTO handleCallback(String code, String state);
}