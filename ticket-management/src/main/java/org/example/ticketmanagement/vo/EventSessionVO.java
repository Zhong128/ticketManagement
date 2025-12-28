// org/example/ticketmanagement/dto/EventSessionVO.java
package org.example.ticketmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSessionVO {
    private Long id;
    private Long eventId;
    private String sessionName;
    private LocalDateTime sessionTime;
    private Integer status;
    private LocalDateTime createTime;

    // 可以添加关联信息
    private String eventName;          // 演出名称
    private String artistName;         // 艺人名称
    private String venue;              // 演出场馆

    // 时间格式化方法（可选）
    public String getFormattedSessionTime() {
        if (sessionTime != null) {
            return sessionTime.toString(); // 或者使用自定义格式
        }
        return null;
    }

    // 判断场次是否已开始（业务方法）
    public boolean isStarted() {
        return sessionTime != null && sessionTime.isBefore(LocalDateTime.now());
    }
}