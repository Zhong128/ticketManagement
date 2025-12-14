package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.*;

import java.util.List;

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
     * 分页查询用户列表
     */
    PageResult<User> listUsers(UserQuery query);

    /**
     * 批量删除用户
     */
    void deleteUsersByIds(List<Long> ids);

    /**
     * 批量修改用户状态
     */
    void updateUsersStatus(List<Long> ids, Integer status);



}
