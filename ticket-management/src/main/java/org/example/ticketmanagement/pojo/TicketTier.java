// org/example/ticketmanagement/pojo/TicketTier.java
package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTier {
    private Long id;                    // 票档ID
    private Long eventId;               // 演出ID（冗余，方便查询）
    private Long sessionId;             // 场次ID
    private String tierName;            // 票档名称（如：VIP票、看台票、学生票）
    private BigDecimal originalPrice;   // 原价
    private BigDecimal currentPrice;    // 现价（可能打折）
    private Integer totalStock;         // 总库存
    private Integer availableStock;     // 可用库存
    private Integer status;             // 状态：0-禁用，1-启用
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}