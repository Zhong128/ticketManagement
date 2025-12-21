package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.AdminUserDTO;
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

@Slf4j
@RestController
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

        try {
            User user = userService.getUserById(id);
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                userDTO.setPassword(null); // 不返回密码
                return Result.success(userDTO);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            log.error("查询用户详情失败: {}", e.getMessage(), e);
            return Result.error("查询失败");
        }
    }


    /**
     * 修改用户信息（管理端）
     */
    @Operation(summary = "修改用户信息", tags = {"管理端/用户管理"})
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody AdminUserDTO adminUserDTO) {
        log.info("管理员修改用户信息: {}", id);

        try {
            // 创建User对象
            User user = new User();
            BeanUtils.copyProperties(adminUserDTO, user);
            // 确保ID一致
            user.setId(id);

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
     * 启用/禁用用户
     */
    @Operation(summary = "启用/禁用用户", tags = {"管理端/用户管理"})
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("管理员修改用户状态: {}, status={}", id, status);

        try {
            boolean success = userService.updateUserStatus(id, status);
            if (success) {
                return Result.success("状态修改成功");
            } else {
                return Result.error("状态修改失败");
            }
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("修改用户状态失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改用户状态失败: {}", e.getMessage(), e);
            return Result.error("状态修改失败");
        }
    }

    /**
     * 批量修改用户状态
     */
    @Operation(summary = "批量修改用户状态", tags = {"管理端/用户管理"})
    @PutMapping("/batch/status")
    public Result<Void> batchUpdateUserStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        log.info("批量修改用户状态，ID列表: {}, status={}", ids, status);

        try {
            boolean success = userService.updateUsersStatus(ids, status);
            if (success) {
                return Result.success("状态修改成功");
            }else {
                return Result.error("状态修改失败");
            }
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("批量修改用户状态失败: {}", e.getMessage());
            return Result.error(e.getMessage());
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

        try {
            boolean success = userService.deleteUserById(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (RuntimeException e) {
            log.warn("删除用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return Result.error("删除失败");
        }
    }


    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户", tags = {"管理端/用户管理"})
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteUsers(@RequestParam List<Long> ids) {
        log.info("批量删除用户，ID列表: {}", ids);

        try {
            boolean success = userService.deleteUsersByIds(ids);
            if (success) {
                return Result.success("批量删除成功");
            }else {
                return Result.error("批量删除失败");
            }
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("批量删除用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量删除用户失败: {}", e.getMessage(), e);
            return Result.error("批量删除失败");
        }
    }
}