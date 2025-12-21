package org.example.ticketmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendCodeRequestDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    // 可以添加业务类型字段，如注册、登录、找回密码等
    private String type = "REGISTER"; // REGISTER, LOGIN, RESET_PASSWORD
}
