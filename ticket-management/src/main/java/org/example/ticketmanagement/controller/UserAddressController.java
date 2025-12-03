package org.example.ticketmanagement.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.pojo.UserAddress;
import org.example.ticketmanagement.service.UserAddressService;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/address")
public class UserAddressController {
    @Autowired
    private UserAddressService UserAddressService;
    @Autowired
    private UserService userService;
    /**
     * 根据用户id查询收货地址
     */
    @GetMapping("{userId}")
    public Result getUserAddressByUserId(@PathVariable Long userId) {
        log.info("根据用户id查询收货地址: {}", userId);
        //先检查用户是否存在
        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        //再检查收货地址是否存在
        UserAddress existingUserAddress = UserAddressService.getUserAddressByUserId(userId);
        if (existingUserAddress == null) {
            return Result.error("用户未添加任何收货地址");
        }
        UserAddress userAddress = UserAddressService.getUserAddressByUserId(userId);
        return Result.success(userAddress);
    }
    /**
     * 新增收货地址
     */
    @PostMapping
    public Result addUserAddress(@Valid @RequestBody UserAddress userAddress) {
        log.info("新增收货地址: {}", userAddress);
        //先检查用户是否存在
        User existingUser = userService.getUserById(userAddress.getUserId());
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        //检查是否为重复地址
        if (UserAddressService.isDuplicateAddress(userAddress)) {
            return Result.error("收货地址已存在");
        }
        UserAddressService.addUserAddress(userAddress);
        return Result.success();
    }
    /**
     * 根据id修改收货地址
     */
    @PutMapping("{id}")
    public Result updateUserAddress(@PathVariable Long id, @RequestBody UserAddress userAddress) {
        log.info("修改收货地址: {}", userAddress);
        //先检查收货地址是否存在
        UserAddress existingUserAddress = UserAddressService.getUserAddressById(id);
        if (existingUserAddress == null) {
            return Result.error("收货地址不存在");
        }

        UserAddressService.updateUserAddressById(id, userAddress);
        return Result.success();
     }
     /**
     * 根据id删除收货地址
     */
     @DeleteMapping("{id}")
     public Result deleteUserAddressById(@PathVariable Long id) {
        log.info("根据id删除收货地址: {}", id);
         //先检查收货地址是否存在
         UserAddress existingUserAddress = UserAddressService.getUserAddressById(id);
         if (existingUserAddress == null) {
             return Result.error("收货地址不存在");
         }
        UserAddressService.deleteUserAddressById(id);
        return Result.success();
     }
    }


