// org/example/ticketmanagement/controller/UserAuthController.java
package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.LoginResponseDTO;
import org.example.ticketmanagement.dto.UserLoginDTO;
import org.example.ticketmanagement.dto.UserLoginOrRegisterDTO;
import org.example.ticketmanagement.dto.UserRegisterDTO;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.AuthService;
import org.example.ticketmanagement.service.UserService;
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
    private UserService userService;

    /**
     * 用户登录接口
     */
//    @Operation(summary = "用户登录", tags = {"客户端/认证注册"})
//    @PostMapping("/login")
//    public Result<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
//        log.info("用户端登录: {}", userLoginDTO.getEmail() != null ? userLoginDTO.getEmail() : userLoginDTO.getUsername());
//
//        try {
//            User user = new User();
//            user.setUsername(userLoginDTO.getUsername());
//            user.setEmail(userLoginDTO.getEmail());
//            user.setPassword(userLoginDTO.getPassword());
//
//            LoginInfo loginInfo = authService.userLogin(user);
//
//            LoginResponseDTO responseDTO = new LoginResponseDTO();
//            responseDTO.setId(loginInfo.getId());
//            responseDTO.setUsername(loginInfo.getUsername());
//            responseDTO.setToken(loginInfo.getToken());
//
//            return Result.success(responseDTO);
//        } catch (RuntimeException e) {
//            log.error("用户登录失败: {}", e.getMessage());
//            return Result.error(e.getMessage());
//        } catch (Exception e) {
//            log.error("用户登录异常: {}", e.getMessage(), e);
//            return Result.error("登录失败，请稍后重试");
//        }
//    }
//
//    /**
//     * 用户注册接口
//     */
//    @Operation(summary = "用户注册", tags = {"客户端/认证注册"})
//    @PostMapping("/register")
//    public Result<Void> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
//        log.info("用户注册: {}", userRegisterDTO.getEmail());
//
//        try {
//            // 转换DTO到Entity
//            User user = new User();
//            user.setUsername(userRegisterDTO.getUsername());
//            user.setPassword(userRegisterDTO.getPassword());
//            user.setEmail(userRegisterDTO.getEmail());
//            user.setPhone(userRegisterDTO.getPhone());
//            user.setNickname(userRegisterDTO.getNickname());
//            user.setRealName(userRegisterDTO.getRealName());
//            user.setGender(userRegisterDTO.getGender());
//            user.setBirthday(userRegisterDTO.getBirthday());
//
//            // 执行注册
//            authService.register(user);
//            return Result.success("注册成功");
//        } catch (RuntimeException e) {
//            log.error("用户注册失败: {}", e.getMessage());
//            return Result.error(e.getMessage());
//        } catch (Exception e) {
//            log.error("用户注册异常: {}", e.getMessage(), e);
//            return Result.error("注册失败，请稍后重试");
//        }
//    }

    /**
     * 统一登录/注册接口
     * 用户输入邮箱密码后：
     * 1. 如果用户存在且密码正确 → 登录成功
     * 2. 如果用户不存在 → 自动注册并登录
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
            user.setUsername(dto.getUsername());
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