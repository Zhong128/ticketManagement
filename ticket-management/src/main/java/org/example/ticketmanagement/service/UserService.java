package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.pojo.UserAddress;

public interface UserService {
    /**
     * 根据id查询用户
     */
    User getUserById(Long id);
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    /**
     * 根据邮箱查询用户
     */
    User getUserByEmail(String email);
    /**
     * 根据手机号查询用户
     */
    User getUserByPhone(String phone);
    /**
     * 新增用户
     */
    void addUser(User user);
    /**
     * 根据id删除用户
     */
    void deleteUserById(Long id);
    /**
     * 修改用户
     */
    void updateUser(User user);
    /**
     * 用户注册
     */
    void registerUser(User user);
    /**
     * 登录
     */
    LoginInfo login(User user);



}
