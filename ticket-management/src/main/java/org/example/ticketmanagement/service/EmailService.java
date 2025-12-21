// org/example/ticketmanagement/service/EmailService.java
package org.example.ticketmanagement.service;

public interface EmailService {
    /**
     * 发送简单文本邮件
     */
    boolean sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送HTML格式邮件
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * 发送验证码邮件
     */
    boolean sendVerificationCodeEmail(String to, String code);

    /**
     * 发送验证码邮件（HTML格式）
     */
    boolean sendVerificationCodeHtmlEmail(String to, String code);
}
