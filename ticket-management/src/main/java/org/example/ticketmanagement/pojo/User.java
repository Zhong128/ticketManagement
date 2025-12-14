package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    private String realName;
    private String idCard;
    private Integer gender;
    private LocalDate birthday;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 微信相关字段
    private String openId;
    private String unionId;
    // 角色字段
    private String role;
}
