package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装登录结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private Long id;
    private String username;
    private String password;
    private String token;
}
