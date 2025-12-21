// org/example/ticketmanagement/controller/user/UserAuthController.java
package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.*;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.AuthService;
import org.example.ticketmanagement.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/user")
@Tag(name = "客户端/认证注册", description = "用户登录、注册、退出等认证相关接口")
public class UserAuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 统一登录/注册接口
     * 1. 新用户：发送验证码到邮箱，验证后完成注册并登录
     * 2. 老用户：直接使用邮箱密码登录
     */
    @Operation(summary = "统一登录/注册", tags = {"客户端/认证模块"})
    @PostMapping("/login-or-register")
    public Result<LoginResponseDTO> loginOrRegister(@Valid @RequestBody UserLoginOrRegisterDTO dto) {
        log.info("统一登录/注册: email={}", dto.getEmail());

        try {
            // 转换为User对象
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setNickname(dto.getNickname());

            // 调用统一登录/注册服务
            LoginInfo loginInfo = authService.loginOrRegister(user);

            // 构建响应
            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setId(loginInfo.getId());
            responseDTO.setUsername(loginInfo.getUsername());
            responseDTO.setToken(loginInfo.getToken());

            // 判断是新注册用户还是已有用户
            String message = loginInfo.getIsNewUser() != null && loginInfo.getIsNewUser()
                    ? "注册并登录成功"
                    : "登录成功";

            return Result.success(message, responseDTO);
        } catch (RuntimeException e) {
            log.error("登录/注册失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("登录/注册异常: {}", e.getMessage(), e);
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 验证码验证接口（新用户注册第二步）
     */
    @Operation(summary = "验证码验证注册", tags = {"客户端/认证模块"})
    @PostMapping("/verify-registration")
    public Result<LoginResponseDTO> verifyRegistration(@Valid @RequestBody LoginWithCodeDTO dto) {
        log.info("验证码验证注册: email={}", dto.getEmail());

        try {
            // 验证验证码
            boolean isValid = verificationCodeService.verifyCode(dto.getEmail(), dto.getCode());
            if (!isValid) {
                return Result.error("验证码错误或已过期");
            }

            // 完成注册并登录
            LoginInfo loginInfo = authService.completeRegistrationWithCode(dto.getEmail());

            // 构建响应
            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setId(loginInfo.getId());
            responseDTO.setUsername(loginInfo.getUsername());
            responseDTO.setToken(loginInfo.getToken());

            return Result.success("注册成功", responseDTO);
        } catch (RuntimeException e) {
            log.error("验证码验证失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("验证码验证异常: {}", e.getMessage(), e);
            return Result.error("验证失败，请稍后重试");
        }
    }

    /**
     * 用户退出登录
     */
    @Operation(summary = "用户退出", tags = {"客户端/认证注册"})
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("用户退出登录");

        try {
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            authService.logout(token);
            return Result.success("退出成功");
        } catch (Exception e) {
            log.error("退出登录异常: {}", e.getMessage(), e);
            return Result.error("退出失败，请稍后重试");
        }
    }
}
