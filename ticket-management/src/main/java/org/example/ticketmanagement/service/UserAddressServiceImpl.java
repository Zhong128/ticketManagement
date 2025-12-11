package org.example.ticketmanagement.service;

import org.example.ticketmanagement.mapper.UserAddressMapper;
import org.example.ticketmanagement.pojo.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAddressServiceImpl implements UserAddressService{
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public UserAddress getUserAddressByUserId(Long userId) {
        return userAddressMapper.getUserAddressByUserId(userId);
    }

    @Override
    public UserAddress getUserAddressById(Long id) {
        return userAddressMapper.getUserAddressById(id);
    }

    @Override
    public boolean isDuplicateAddress(UserAddress userAddress) {
        return userAddressMapper.countDuplicateAddress(userAddress) > 0;
    }


    @Override
    public void addUserAddress(UserAddress userAddress) {
        userAddress.setCreateTime(LocalDateTime.now());
        // TODO：后续这些重复的代码，可以考虑使用AOP切面统一修改
        userAddress.setUpdateTime(LocalDateTime.now());
        userAddressMapper.addUserAddress(userAddress);
    }

    @Override
    public void updateUserAddressById(Long id, UserAddress userAddress) {
        userAddress.setId(id);
        // TODO：同上，其他的实体类都一样
        userAddress.setUpdateTime(LocalDateTime.now());
        userAddressMapper.updateUserAddressById(userAddress);
    }

    @Override
    public void deleteUserAddressById(Long id) {
        userAddressMapper.deleteUserAddressById(id);
    }
}
