package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private Long id;
    private String username;
    private String email;
    private String token;

    // 新增字段，标识是否是新注册的用户
    private Boolean isNewUser;

    // 保持原有构造函数兼容性
    public LoginInfo(Long id, String username, String email, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
        this.isNewUser = false;
    }

}