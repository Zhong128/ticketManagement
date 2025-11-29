package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.UserAddress;

public interface UserAddressService {
    /**
     * 根据用户id查询收货地址
     */
    UserAddress getUserAddressByUserId(Long userId);
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
