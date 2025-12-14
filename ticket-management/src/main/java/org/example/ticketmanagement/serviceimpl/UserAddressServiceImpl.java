package org.example.ticketmanagement.serviceimpl;

import org.example.ticketmanagement.mapper.UserAddressMapper;
import org.example.ticketmanagement.pojo.UserAddress;
import org.example.ticketmanagement.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public UserAddress getUserAddressByUserId(Long userId) {
        return userAddressMapper.getUserAddressByUserId(userId);
    }

    @Override
    public List<UserAddress> listUserAddressesByUserId(Long userId) {
        return userAddressMapper.listUserAddressesByUserId(userId);
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

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        // 先清除该用户的所有默认地址标记
        userAddressMapper.clearDefaultAddress(userId);

        // 设置新的默认地址
        userAddressMapper.setDefaultAddress(addressId);
    }
}

