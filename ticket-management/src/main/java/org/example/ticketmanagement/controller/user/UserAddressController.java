package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.UserAddress;
import org.example.ticketmanagement.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user/address")
@Tag(name = "客户端/收货中心", description = "用户收货地址管理相关接口")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取当前用户的所有收货地址
     */
    @Operation(summary = "获取收货地址列表", tags = {"客户端/收货中心"})
    @GetMapping
    public Result<List<UserAddress>> getUserAddresses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取用户收货地址，用户ID: {}", userId);

        List<UserAddress> addresses = userAddressService.listUserAddressesByUserId(userId);
        return Result.success(addresses);
    }

    /**
     * 获取默认收货地址
     */
    @Operation(summary = "获取默认地址", tags = {"客户端/收货中心"})
    @GetMapping("/default")
    public Result<UserAddress> getDefaultAddress(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取默认收货地址，用户ID: {}", userId);

        // 先查询所有地址，然后筛选出默认的
        List<UserAddress> addresses = userAddressService.listUserAddressesByUserId(userId);
        UserAddress defaultAddress = addresses.stream()
                .filter(address -> address.getIsDefault() != null && address.getIsDefault() == 1)
                .findFirst()
                .orElse(null);

        return Result.success(defaultAddress);
    }

    /**
     * 根据ID获取收货地址详情
     */
    @Operation(summary = "根据ID获取收货地址", tags = {"客户端/收货中心"})
    @GetMapping("/{id}")
    public Result<UserAddress> getUserAddressById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取收货地址详情，地址ID: {}, 用户ID: {}", id, userId);

        UserAddress address = userAddressService.getUserAddressById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.error("收货地址不存在或无权访问");
        }

        return Result.success(address);
    }

    /**
     * 新增收货地址
     */
    @Operation(summary = "新增收货地址", tags = {"客户端/收货中心"})
    @PostMapping
    public Result<Void> addUserAddress(@Valid @RequestBody UserAddress userAddress, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("新增收货地址，用户ID: {}", userId);

        // 设置当前用户ID
        userAddress.setUserId(userId);

        // 如果这是第一个地址，设为默认
        List<UserAddress> existingAddresses = userAddressService.listUserAddressesByUserId(userId);
        if (existingAddresses.isEmpty()) {
            userAddress.setIsDefault(1); // 设为默认地址
        } else {
            userAddress.setIsDefault(0); // 非默认地址
        }

        // 检查是否为重复地址（可选）
        if (userAddressService.isDuplicateAddress(userAddress)) {
            return Result.error("收货地址已存在");
        }

        userAddressService.addUserAddress(userAddress);
        return Result.success();
    }

    /**
     * 修改收货地址
     */
    @Operation(summary = "修改收货地址", tags = {"客户端/收货中心"})
    @PutMapping("/{id}")
    public Result<Void> updateUserAddress(@PathVariable Long id,
                                          @Valid @RequestBody UserAddress userAddress,
                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改收货地址，地址ID: {}, 用户ID: {}", id, userId);

        // 检查地址是否存在且属于当前用户
        UserAddress existingAddress = userAddressService.getUserAddressById(id);
        if (existingAddress == null || !existingAddress.getUserId().equals(userId)) {
            return Result.error("收货地址不存在或无权操作");
        }

        // 确保用户ID不变
        userAddress.setUserId(userId);
        userAddressService.updateUserAddressById(id, userAddress);
        return Result.success();
    }

    /**
     * 设置默认收货地址
     */
    @Operation(summary = "设置默认收货地址", tags = {"客户端/收货中心"})
    @PutMapping("/{id}/default")
    public Result<Void> setDefaultAddress(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("设置默认收货地址，地址ID: {}, 用户ID: {}", id, userId);

        // 检查地址是否存在且属于当前用户
        UserAddress existingAddress = userAddressService.getUserAddressById(id);
        if (existingAddress == null || !existingAddress.getUserId().equals(userId)) {
            return Result.error("收货地址不存在或无权操作");
        }

        userAddressService.setDefaultAddress(userId, id);
        return Result.success();
    }

    /**
     * 删除收货地址
     */
    @Operation(summary = "删除收货地址", tags = {"客户端/收货中心"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteUserAddressById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("删除收货地址，地址ID: {}, 用户ID: {}", id, userId);

        // 检查地址是否存在且属于当前用户
        UserAddress existingAddress = userAddressService.getUserAddressById(id);
        if (existingAddress == null || !existingAddress.getUserId().equals(userId)) {
            return Result.error("收货地址不存在或无权操作");
        }

        // 如果是默认地址，需要处理（可选：可以设置为不允许删除默认地址）
        if (existingAddress.getIsDefault() != null && existingAddress.getIsDefault() == 1) {
            return Result.error("默认地址不能删除，请先设置其他地址为默认");
        }

        userAddressService.deleteUserAddressById(id);
        return Result.success();
    }
}