package org.example.ticketmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.pojo.UserAddress;
import org.example.ticketmanagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
// TODO：看上去逻辑基本上都放在controller里了，代码规范要注意
// TODO：也是一样的，增删改操作，需要在方法上加上注释@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper UserMapper;
    @Override
    public User getUserById(Long id) {
        return UserMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return UserMapper.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return UserMapper.getUserByEmail(email);
    }

    @Override
    public User getUserByPhone(String phone) {
        return UserMapper.getUserByPhone(phone);
    }

    @Override
    public void addUser(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        UserMapper.addUser(user);

    }

    @Override
    public void deleteUserById(Long id) {
        UserMapper.deleteUserById(id);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        UserMapper.updateUser(user);
    }
    @Override
    public void registerUser(User user) {
        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1); // 默认启用状态
        user.setLastLoginTime(LocalDateTime.now());
        // 可以设置默认头像等

        UserMapper.registerUser(user);
    }


    @Override
    public LoginInfo login(User user) {
        User u = null;

        // 判断是使用用户名还是邮箱登录
        // TODO：不用用户名了
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            // 用户名登录
            u = UserMapper.selectByUsernameAndPassword(user);
        } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            // 邮箱登录
            u = UserMapper.selectByEmailAndPassword(user);
        }

        if (u != null){
            log.info("用户登录成功: {}", u);
            //生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            //添加自定义信息
            // TODO：这边加了自定义信息，那是否能在拦截器中解析token后，进一步处理呢？
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            String jwt = JwtUtils.generateToken(claims);
            return new LoginInfo(u.getId(), u.getUsername(), null, jwt);
        }


        return null;
    }


}
