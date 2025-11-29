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
    public Result addUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("新增用户: {}", userDTO);
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
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 修改用户
     */
    @PutMapping
    public Result updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("修改用户: {}", userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.updateUser(user);
        return Result.success();
    }
}

