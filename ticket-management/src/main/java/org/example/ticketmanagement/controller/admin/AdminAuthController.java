// org/example/ticketmanagement/controller/AdminAuthController.java
package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.LoginResponseDTO;
import org.example.ticketmanagement.dto.UserLoginDTO;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/admin")
@Tag(name = "管理端/认证授权", description = "管理员登录、退出等认证相关接口")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /**
     * 管理员登录接口
     */
    @Operation(summary = "管理员登录", tags = {"管理端/认证授权"})
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("管理端登录: {}", userLoginDTO.getEmail());

        try {
            User user = new User();
            user.setEmail(userLoginDTO.getEmail());
            user.setPassword(userLoginDTO.getPassword());

            LoginInfo loginInfo = authService.adminLogin(user);

            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setId(loginInfo.getId());
            responseDTO.setUsername(loginInfo.getUsername());
            responseDTO.setToken(loginInfo.getToken());

            return Result.success(responseDTO);
        } catch (RuntimeException e) {
            log.error("管理员登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("管理员登录异常: {}", e.getMessage(), e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 管理员退出登录
     */
    @Operation(summary = "管理员退出", tags = {"管理端/认证授权"})
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("管理员退出登录");

        try {
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            authService.logout(token);
            return Result.success("退出成功");
        } catch (Exception e) {
            log.error("管理员退出登录异常: {}", e.getMessage(), e);
            return Result.error("退出失败，请稍后重试");
        }
    }
}