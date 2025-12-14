package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.UserDTO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.pojo.UserQuery;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/admin/user")
@Validated
@Tag(name = "管理端/用户管理", description = "管理员用户管理相关接口")
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户信息
     */
    @Operation(summary = "分页查询用户列表", tags = {"管理端/用户管理"})
    @GetMapping
    public Result<PageResult<UserDTO>> listUsers(@Valid UserQuery query) {
        log.info("分页查询用户，参数: {}", query);

        try {
            // 查询用户数据
            PageResult<User> pageResult = userService.listUsers(query);

            // 转换为UserDTO
            List<UserDTO> userDTOs = pageResult.getRecords().stream().map(user -> {
                UserDTO dto = new UserDTO();
                BeanUtils.copyProperties(user, dto);
                // 敏感信息不返回
                dto.setPassword(null);
                return dto;
            }).collect(Collectors.toList());

            // 创建新的分页结果
            PageResult<UserDTO> result = new PageResult<>(
                    userDTOs,
                    pageResult.getTotal(),
                    query.getPage(),
                    query.getSize()
            );

            return Result.success(result);

        } catch (IllegalArgumentException e) {
            log.error("分页参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage(), e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    /**
     * 查询用户详情（管理端）
     */
    @Operation(summary = "查询用户详情", tags = {"管理端/用户管理"})
    @GetMapping("/{id}")
    public Result<UserDTO> getUserDetail(@PathVariable Long id) {
        log.info("管理员查询用户详情: {}", id);

        User user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null); // 不返回密码
            return Result.success(userDTO);
        }
        return Result.error("用户不存在");
    }

    /**
     * 修改用户信息（管理端）
     */
    @Operation(summary = "修改用户信息", tags = {"管理端/用户管理"})
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("管理员修改用户信息: {}", id);

        // 检查用户是否存在
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        userDTO.setId(id);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 保留原密码（如果不传密码则不变）
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }

        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 启用/禁用用户
     */
    @Operation(summary = "启用/禁用用户", tags = {"管理端/用户管理"})
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("管理员修改用户状态: {}, status={}", id, status);

        if (status != 0 && status != 1) {
            return Result.error("状态值不合法");
        }

        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        List<Long> ids = List.of(id);
        userService.updateUsersStatus(ids, status);
        return Result.success();
    }

    /**
     * 批量修改用户状态
     */
    @Operation(summary = "批量修改用户状态", tags = {"管理端/用户管理"})
    @PutMapping("/batch/status")
    public Result<Void> batchUpdateUserStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        log.info("批量修改用户状态，ID列表: {}, status={}", ids, status);

        if (ids == null || ids.isEmpty()) {
            return Result.error("用户ID列表不能为空");
        }

        if (status != 0 && status != 1) {
            return Result.error("状态值不合法");
        }

        try {
            userService.updateUsersStatus(ids, status);
            return Result.success("批量修改成功");
        } catch (Exception e) {
            log.error("批量修改用户状态失败: {}", e.getMessage(), e);
            return Result.error("批量修改失败");
        }
    }

    /**
     * 删除用户（管理端）
     */
    @Operation(summary = "删除用户", tags = {"管理端/用户管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        log.info("管理员删除用户: {}", id);

        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户", tags = {"管理端/用户管理"})
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteUsers(@RequestParam List<Long> ids) {
        log.info("批量删除用户，ID列表: {}", ids);

        if (ids == null || ids.isEmpty()) {
            return Result.error("用户ID列表不能为空");
        }

        try {
            userService.deleteUsersByIds(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除用户失败: {}", e.getMessage(), e);
            return Result.error("批量删除失败");
        }
    }
}