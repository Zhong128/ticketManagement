package org.example.ticketmanagement.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.LoginResponseDTO;
import org.example.ticketmanagement.dto.UserLoginDTO;
import org.example.ticketmanagement.dto.UserRegisterDTO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.UserService;
import org.example.ticketmanagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录: {}", userLoginDTO);

        // 转换DTO到Entity
        User user = new User();
        user.setUsername(userLoginDTO.getUsername());
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());

        org.example.ticketmanagement.pojo.LoginInfo loginInfo = userService.login(user);
        if (loginInfo != null){
            // 转换为响应DTO
            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setId(loginInfo.getId());
            responseDTO.setUsername(loginInfo.getUsername());
            responseDTO.setToken(loginInfo.getToken());
            return Result.success(responseDTO);
        }
        return Result.error("用户名或密码错误");
    }
    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // TODO：登出这样做是为了什么，目前看好像只是打印日志？
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = JwtUtils.parseToken(token);
                log.info("用户退出: ID={}", claims.get("id"));
            } catch (Exception e) {
                // Token可能已过期，不处理
            }
        }
        return Result.success("退出成功", null);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        log.info("用户注册: {}", userRegisterDTO);

        // 转换DTO到Entity
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPhone(userRegisterDTO.getPhone());
        user.setNickname(userRegisterDTO.getNickname());
        user.setRealName(userRegisterDTO.getRealName());
        user.setGender(userRegisterDTO.getGender());
        user.setBirthday(userRegisterDTO.getBirthday());

        // 检查用户名是否已存在
        // TODO：如果是注册的话，按照逻辑来说应该只有邮箱注册来着？
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            User existingUser = userService.getUserByUsername(user.getUsername());
            if (existingUser != null){
                return Result.error("用户已被注册");
            }
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            User existingEmail = userService.getUserByEmail(user.getEmail());
            if (existingEmail != null){
                return Result.error("邮箱已被注册");
            }
        }

        // 执行注册
        userService.registerUser(user);
        return Result.success("注册成功", null);
    }
}
