package org.example.ticketmanagement.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.pojo.UserQuery;
import org.example.ticketmanagement.service.UserService;
import org.example.ticketmanagement.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
// TODO：看上去逻辑基本上都放在controller里了，代码规范要注意
// TODO：也是一样的，增删改操作，需要在方法上加上注释@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        // 如果没有设置角色，默认为USER
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        userMapper.addUser(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userMapper.deleteUserById(id);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    @Override
    public void registerUser(User user) {
        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1); // 默认启用状态
        user.setLastLoginTime(LocalDateTime.now());
        user.setRole("USER"); // 默认角色为用户

        userMapper.registerUser(user);
    }

    @Override
    public PageResult<User> listUsers(UserQuery query) {
        log.info("分页查询用户，参数: {}", query);

        // 验证分页参数
        if (!query.validate()) {
            throw new IllegalArgumentException("分页参数不合法");
        }

        // 查询数据
        List<User> users = userMapper.selectUsersByPage(query);

        // 查询总数
        Long total = userMapper.countUsers(query);

        // 构建分页结果
        return new PageResult<>(users, total, query);
    }

    @Override
    @Transactional
    public void deleteUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("用户ID列表不能为空");
        }

        log.info("批量删除用户，ID列表: {}", ids);

        // TODO: 检查用户是否有未完成的订单等业务约束

        // 批量删除
        for (Long id : ids) {
            userMapper.deleteUserById(id);
        }
    }

    @Override
    @Transactional
    public void updateUsersStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("用户ID列表不能为空");
        }

        if (status != 0 && status != 1) {
            throw new IllegalArgumentException("状态值必须是0或1");
        }

        log.info("批量修改用户状态，ID列表: {}, 状态: {}", ids, status);

        // 查询用户列表
        List<User> users = userMapper.selectUsersByIds(ids);

        // 批量更新状态
        for (User user : users) {
            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateUser(user);
        }
    }


}
