package org.example.ticketmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "用户登录/注册请求DTO")
public class UserLoginOrRegisterDTO {

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    @Schema(description = "自动注册时使用的用户名（选填，不填则使用邮箱前缀）")
    private String username;

    @Schema(description = "自动注册时使用的昵称（选填，不填则使用用户名）")
    private String nickname;
}