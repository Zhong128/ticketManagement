// org/example/ticketmanagement/serviceimpl/AuthServiceImpl.java
package org.example.ticketmanagement.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.Token.TokenManager;
import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.LoginInfo;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.AuthService;
import org.example.ticketmanagement.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public LoginInfo login(User user) {
        User u = null;

        // 判断是使用用户名还是邮箱登录
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            u = userMapper.selectByUsernameAndPassword(user);
        } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            u = userMapper.selectByEmailAndPassword(user);
        }

        if (u != null && u.getStatus() == 1) {
            log.info("登录成功: {}", u.getUsername());

            // 更新最后登录时间
            u.setLastLoginTime(LocalDateTime.now());
            userMapper.updateUser(u);

            // 生成JWT令牌（包含角色信息）
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", u.getId());
            claims.put("username", u.getUsername());
            claims.put("role", u.getRole() != null ? u.getRole() : "USER");

            String jwt = JwtUtils.generateToken(claims);
            return new LoginInfo(u.getId(), u.getUsername(), null, jwt);
        } else if (u != null && u.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        throw new RuntimeException("用户名或密码错误");
    }

    @Override
    public LoginInfo userLogin(User user) {
        LoginInfo loginInfo = login(user);

        // 验证用户角色
        User u = userMapper.getUserById(loginInfo.getId());
        if (!"USER".equals(u.getRole())) {
            throw new RuntimeException("非用户角色，请使用用户端登录");
        }

        return loginInfo;
    }

    @Override
    public LoginInfo adminLogin(User user) {
        LoginInfo loginInfo = login(user);

        // 验证管理员角色
        User u = userMapper.getUserById(loginInfo.getId());
        if (!"ADMIN".equals(u.getRole())) {
            throw new RuntimeException("非管理员角色，请使用管理端登录");
        }

        return loginInfo;
    }

    @Override
    public void logout(String token) {
        if (token != null && !token.isEmpty()) {
            tokenManager.addToBlacklist(token);
        }
    }

    @Override
    public void register(User user) {
        // 检查邮箱是否已存在
        User existingEmail = userMapper.getUserByEmail(user.getEmail());
        if (existingEmail != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1); // 默认启用状态
        user.setLastLoginTime(LocalDateTime.now());
        user.setRole("USER"); // 注册用户默认角色为USER

        // 生成默认用户名（使用邮箱前缀）
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            String emailPrefix = user.getEmail().split("@")[0];
            user.setUsername(emailPrefix);
        }

        // 检查用户名是否已存在，如果存在则添加随机后缀
        User existingUsername = userMapper.getUserByUsername(user.getUsername());
        if (existingUsername != null) {
            user.setUsername(user.getUsername() + "_" + System.currentTimeMillis() % 1000);
        }

        userMapper.registerUser(user);
    }

    @Override
    public LoginInfo loginOrRegister(User user) {
        log.info("登录/注册请求: email={}", user.getEmail());

        // 1. 首先尝试登录
        try {
            // 构建一个只包含邮箱和密码的User对象用于登录验证
            User loginUser = new User();
            loginUser.setEmail(user.getEmail());
            loginUser.setPassword(user.getPassword());

            return userLogin(loginUser);
        } catch (RuntimeException loginException) {
            // 2. 登录失败，检查是否是用户不存在
            log.info("登录失败，尝试注册: {}", loginException.getMessage());

            // 检查是否是"用户不存在"或"用户名或密码错误"的情况
            User existingUser = userMapper.getUserByEmail(user.getEmail());
            if (existingUser != null) {
                // 用户存在但登录失败，可能是密码错误或其他原因，直接抛出异常
                throw loginException;
            }

            // 3. 用户不存在，进行注册
            try {
                // 设置注册所需的基本信息
                if (user.getUsername() == null || user.getUsername().isEmpty()) {
                    // 使用邮箱前缀作为用户名
                    String emailPrefix = user.getEmail().split("@")[0];
                    user.setUsername(emailPrefix);
                }

                if (user.getNickname() == null || user.getNickname().isEmpty()) {
                    user.setNickname(user.getUsername());
                }

                user.setRole("USER");
                user.setStatus(1);
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                user.setLastLoginTime(LocalDateTime.now());

                // 执行注册
                register(user);
                log.info("新用户注册成功: email={}, username={}", user.getEmail(), user.getUsername());

                // 注册成功后登录
                LoginInfo loginInfo = userLogin(user);
                loginInfo.setIsNewUser(true); // 标记为新用户

                return loginInfo;

            } catch (DuplicateKeyException e) {
                // 并发情况下，可能其他请求已经注册了该用户，此时重新尝试登录
                log.info("注册时发现用户已存在（并发情况），重新尝试登录");
                return userLogin(user);
            } catch (Exception e) {
                log.error("注册失败: {}", e.getMessage(), e);
                throw new RuntimeException("注册失败: " + e.getMessage());
            }
        }
    }
}