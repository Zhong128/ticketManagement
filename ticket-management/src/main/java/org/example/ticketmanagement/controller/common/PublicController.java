package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.ticketmanagement.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Tag(name = "公共接口", description = "无需认证的公共接口")
public class PublicController {

    /**
     * 验证邮箱是否已注册
     */
    @Operation(summary = "检查邮箱是否已注册", tags = {"公共接口"})
    @GetMapping("/email/check")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        // TODO: 实现邮箱检查逻辑
        return Result.success(false);
    }

    /**
     * 验证手机号是否已注册
     */
    @Operation(summary = "检查手机号是否已注册", tags = {"公共接口"})
    @GetMapping("/phone/check")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        // TODO: 实现手机号检查逻辑
        return Result.success(false);
    }
}