package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.UserAddress;

public interface UserAddressService {
    /**
     * 根据用户id查询收货地址
     */
    UserAddress getUserAddressByUserId(Long userId);
    /**
     * 根据id查询收货地址
     */
    UserAddress getUserAddressById(Long id);
    /**
     * 检查是否存在重复的收货地址
     */
    boolean isDuplicateAddress(UserAddress userAddress);
    /**
     * 新增收货地址
     */
    void addUserAddress(UserAddress userAddress);
    /**
     * 根据id修改收货地址
     */
    void updateUserAddressById(Long id, UserAddress userAddress);
    /**
     * 根据id删除收货地址
     */
    void deleteUserAddressById(Long id);


}
