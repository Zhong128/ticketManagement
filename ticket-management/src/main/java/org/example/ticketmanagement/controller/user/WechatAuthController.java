package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.WechatLoginDTO;
import org.example.ticketmanagement.dto.WechatLoginResultDTO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth/wechat")
@Tag(name = "客户端/微信登录", description = "微信登录相关接口")
public class WechatAuthController {

    @Autowired
    private WechatService wechatService;

    /**
     * 获取微信登录二维码URL
     */
    @Operation(summary = "获取微信登录二维码", tags = {"客户端/微信登录"})
    @GetMapping("/qrcode")
    public Result<Map<String, String>> getQrCode() {
        try {
            String qrCodeUrl = wechatService.getLoginQrCode();
            Map<String, String> data = new HashMap<>();
            data.put("url", qrCodeUrl);//使用Map封装返回给前端(data类型必须为object)
            return Result.success("获取二维码URL成功", data);
        } catch (Exception e) {
            log.error("获取微信二维码失败", e);
            return Result.error("获取微信登录二维码失败");
        }
    }


    /**
     * 微信授权回调接口
     */
    @Operation(summary = "微信登录回调", tags = {"客户端/微信登录"})
    @PostMapping("/callback")
    public Result<WechatLoginResultDTO> callback(@RequestBody WechatLoginDTO loginDTO) {
        try {
            WechatLoginResultDTO result = wechatService.handleCallback(loginDTO.getCode(), loginDTO.getState());
            if (result.isSuccess()) {
                String message = result.isNewUser() ? "微信登录成功(新用户)" : "微信登录成功";
                return Result.success(message, result);
            } else {
                return Result.error(result.getMessage());
            }
        } catch (Exception e) {
            log.error("微信登录回调处理失败", e);
            return Result.error("微信登录处理失败");
        }
    }
}
