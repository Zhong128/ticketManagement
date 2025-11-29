package org.example.ticketmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.UserAddress;
import org.example.ticketmanagement.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/address")
public class UserAddressController {
    @Autowired
    private UserAddressService UserAddressService;
    /**
     * 根据用户id查询收货地址
     */
    @GetMapping("{userId}")
    public Result getUserAddressByUserId(@PathVariable Long userId) {
        log.info("根据用户id查询收货地址: {}", userId);
        UserAddress userAddress = UserAddressService.getUserAddressByUserId(userId);
        return Result.success(userAddress);
    }
    /**
     * 新增收货地址
     */
    @PostMapping
    public Result addUserAddress(@RequestBody UserAddress userAddress) {
        log.info("新增收货地址: {}", userAddress);
        UserAddressService.addUserAddress(userAddress);
        return Result.success();
    }
    /**
     * 根据id修改收货地址
     */
    @PutMapping("{id}")
    public Result updateUserAddress(@PathVariable Long id, @RequestBody UserAddress userAddress) {
        log.info("修改收货地址: {}", userAddress);
        UserAddressService.updateUserAddressById(id, userAddress);
        return Result.success();
     }
     /**
     * 根据id删除收货地址
     */
     @DeleteMapping("{id}")
     public Result deleteUserAddressById(@PathVariable Long id) {
        log.info("根据id删除收货地址: {}", id);
        UserAddressService.deleteUserAddressById(id);
        return Result.success();
     }
    }


