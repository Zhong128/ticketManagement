package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSession {
    private Long id;                    // 场次ID
    private Long eventId;               // 演出ID
    private String sessionName;         // 场次名称（如：第一场、晚场、加场等）
    private LocalDateTime sessionTime;  // 场次时间
    private Integer status;             // 状态：0-禁用，1-启用
    private LocalDateTime createTime;   // 创建时间
}