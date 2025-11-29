package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification {
    private Long id;
    private String email;
    private String code;
    private Integer type;
    private Integer isUsed;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
}
