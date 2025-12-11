package org.example.ticketmanagement.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.UserDTO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.User;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//@RestController
//@Slf4j
//@RequestMapping("/user")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 根据id查询用户
//     */
//    @GetMapping("/{id}")
//    public Result getUserById(@PathVariable Long id) {
//        log.info("根据id查询用户: {}", id);
//        User user = userService.getUserById(id);
//        return Result.success(user);
//    }
//        /**
//         * 根据用户名查询用户
//         */
//        @GetMapping
//        public Result getUserByUsername(@RequestBody User  user) {
//            log.info("根据用户名查询用户: {}", user.getUsername());
//            userService.getUserByUsername(user.getUsername());
//            return Result.success(user);
//        }
//        /**
//         * 新增用户
//         */
//        @PostMapping
//        public Result addUser(@RequestBody User user) {
//            log.info("新增用户: {}", user);
//            userService.addUser(user);
//            return Result.success();
//        }
//        /**
//         * 根据id删除用户
//         */
//        @DeleteMapping("/{id}")
//        public Result deleteUserById(@PathVariable Long id) {
//            log.info("根据id删除用户: {}", id);
//            userService.deleteUserById(id);
//            return Result.success();
//        }
//        /**
//         * 修改用户
//         */
//        @PutMapping
//        public Result updateUser(@RequestBody User user) {
//            log.info("修改用户: {}", user);
//            userService.updateUser(user);
//            return Result.success();
//        }
//
//
//
//
//}

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id查询用户
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        log.info("根据id查询用户: {}", id);
        User user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return Result.success(userDTO);
        }
        return Result.error("用户不存在");
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping
    public Result getUserByUsername(@RequestParam String username) {
        log.info("根据用户名查询用户: {}", username);
        User user = userService.getUserByUsername(username);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return Result.success(userDTO);
        }
        return Result.error("用户不存在");
    }

    /**
     * 新增用户
     */
    @PostMapping
    // TODO: Controller应该只负责接收请求、调用Service、返回响应
    // TODO：我看到有注册接口了，那这个接口是用来做什么的
    // TODO：SpringSecurity很重，Satoken ｜ RBAC 角色权限模型
    public Result addUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("新增用户: {}", userDTO);

        // 检查用户名是否已存在
        // TODO：其实不是特别复杂的数据库操作之外，最耗时的是数据库连接事件，如果一个请求，需要三次查询操作，性能不好，如果数据不是很多的情况下可以考虑用OR
        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            User existingUser = userService.getUserByUsername(userDTO.getUsername());
            if (existingUser != null) {
                return Result.error("用户名已存在");
            }
        }
        // 检查邮箱是否已存在
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            User existingEmail = userService.getUserByEmail(userDTO.getEmail());
            if (existingEmail != null) {
                return Result.error("邮箱已存在");
            }
        }
        //检查手机号是否已存在
        // TODO：好像没有手机号登录？
        if (userDTO.getPhone() != null && !userDTO.getPhone().isEmpty()) {
            User existingPhone = userService.getUserByPhone(userDTO.getPhone());
            if (existingPhone != null) {
                return Result.error("手机号已存在");
            }

        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.addUser(user);
        return Result.success();
    }

    /**
     * 根据id删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable Long id) {
        log.info("根据id删除用户: {}", id);

        // 先检查用户是否存在
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 修改用户
     */
    @PutMapping
    public Result updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("修改用户: {}", userDTO);
        // 检查用户是否存在
        User existingUser = userService.getUserById(userDTO.getId());
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.updateUser(user);
        return Result.success();
    }
}

