// org/example/ticketmanagement/pojo/Event.java
package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long id;                     // 演出ID
    private String name;                 // 演出名称
    private String artistName;           // 明星/艺人名称
    private Long categoryId;             // 分类ID
    private Long cityId;                 // 城市ID
    private String venue;                // 演出场馆
    private String coverImage;          // 封面图URL
    private String description;         // 演出描述
    private Integer status;             // 状态：0-草稿，1-已发布，2-已结束
    private LocalDateTime saleStartTime; // 开售时间
    private LocalDateTime saleEndTime;   // 停售时间
    private LocalDateTime eventStartTime; // 演出开始时间
    private LocalDateTime eventEndTime;  // 演出结束时间
    private LocalDateTime createTime;    // 创建时间
    private LocalDateTime updateTime;    // 更新时间
}