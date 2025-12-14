// org/example/ticketmanagement/controller/UserController.java
package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.UserDTO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Tag(name = "客户端/用户中心", description = "用户个人信息管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", tags = {"客户端/用户中心"})
    @GetMapping("/profile")
    public Result<UserDTO> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取当前用户信息，用户ID: {}", userId);

        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null); // 不返回密码

            return Result.success(userDTO);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage(), e);
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 修改个人信息
     */
    @Operation(summary = "修改个人信息", tags = {"客户端/用户中心"})
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改个人信息，用户ID: {}", userId);

        try {
            // 确保只能修改自己的信息
            userDTO.setId(userId);
            // 不允许修改角色和状态
            userDTO.setRole(null);
            userDTO.setStatus(null);

            User existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            User user = new User();
            BeanUtils.copyProperties(userDTO, user);

            // 保留原密码（如果不传密码则不变）
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }

            userService.updateUser(user);
            return Result.success("修改成功");
        } catch (Exception e) {
            log.error("修改用户信息失败: {}", e.getMessage(), e);
            return Result.error("修改失败");
        }
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", tags = {"客户端/用户中心"})
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改密码，用户ID: {}", userId);

        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证旧密码
            if (!user.getPassword().equals(oldPassword)) {
                return Result.error("原密码错误");
            }

            // 更新密码
            user.setPassword(newPassword);
            userService.updateUser(user);

            return Result.success("密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage(), e);
            return Result.error("密码修改失败");
        }
    }
}