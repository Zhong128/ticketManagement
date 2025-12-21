package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.UserDTO;
import org.example.ticketmanagement.dto.UserProfileDTO;
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
    public Result<Void> updateProfile(@Valid @RequestBody UserProfileDTO profileDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改个人信息，用户ID: {}", userId);

        try {
            // 确保只能修改自己的信息
            profileDTO.setId(userId);

            // 创建User对象并复制属性
            User user = new User();
            BeanUtils.copyProperties(user, profileDTO);

            boolean success = userService.updateUser(user);
            if (success) {
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败");
            }
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("修改用户信息失败: {}", e.getMessage());
            return Result.error(e.getMessage());
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
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("修改密码失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage(), e);
            return Result.error("密码修改失败");
        }
    }

    /**
     * 检查用户名是否可用
     * 用于前端在注册或者修改信息时进行用户名查重检查
     */
    @Operation(summary = "检查用户名是否可用", tags = {"客户端/用户中心"})
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        log.debug("检查用户名是否可用: {}", username);

        try {
            boolean exists = userService.isUsernameExists(username);
            boolean isAvailable = !exists; // 用户名是否存在，如果存在，则返回false（不可用）
            return Result.success(isAvailable);
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage(), e);
            return Result.error("检查失败");
        }
    }

    /**
     * 检查邮箱是否可用
     * 用于前端在注册或者修改信息时进行邮箱查重检查
     */
    @Operation(summary = "检查邮箱是否可用", tags = {"客户端/用户中心"})
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        log.debug("检查邮箱是否可用: {}", email);

        try {
            boolean exists = userService.isEmailExists(email);
            return Result.success(!exists); // 如果不存在返回true（可用）
        } catch (Exception e) {
            log.error("检查邮箱失败: {}", e.getMessage(), e);
            return Result.error("检查失败");
        }
    }
}