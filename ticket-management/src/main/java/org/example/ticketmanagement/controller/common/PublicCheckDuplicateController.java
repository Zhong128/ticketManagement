package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/public")
@Tag(name = "公共功能型接口/查重校验", description = "无需认证的公共接口")
public class PublicCheckDuplicateController {

    @Autowired
    private UserService userService;

    /**
     * 验证邮箱是否已注册
     */
    @Operation(summary = "检查邮箱是否已注册", tags = {"公共功能型接口/邮箱校验"})
    @GetMapping("/email/check")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        log.info("检查邮箱是否已注册: {}", email);
        try {
            boolean exists = userService.isEmailExists(email);
            return Result.success("查询成功", exists);
        } catch (Exception e) {
            log.error("检查邮箱是否存在失败: {}", e.getMessage(), e);
            return Result.error("查询失败");
        }
    }
}