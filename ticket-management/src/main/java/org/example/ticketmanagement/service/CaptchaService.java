// org/example/ticketmanagement/service/CaptchaService.java
package org.example.ticketmanagement.service;

import org.example.ticketmanagement.dto.CaptchaResult;

import java.awt.image.BufferedImage;

public interface CaptchaService {
    /**
     * 生成图形验证码
     */
    CaptchaResult generateCaptcha();

    /**
     * 验证图形验证码
     */
    boolean validateCaptcha(String captchaKey, String userInput);
}
