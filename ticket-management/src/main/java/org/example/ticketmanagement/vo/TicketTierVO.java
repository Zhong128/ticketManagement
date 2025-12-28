// org/example/ticketmanagement/dto/TicketTierVO.java
package org.example.ticketmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTierVO {
    private Long id;
    private Long eventId;
    private Long sessionId;
    private String tierName;
    private BigDecimal originalPrice;
    private BigDecimal currentPrice;
    private Integer totalStock;
    private Integer availableStock;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联信息（可选）
    private String eventName;          // 演出名称
    private String sessionName;        // 场次名称
    private LocalDateTime sessionTime; // 场次时间

    // 计算折扣（业务方法）
    public BigDecimal getDiscount() {
        if (originalPrice != null && currentPrice != null
                && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            return originalPrice.subtract(currentPrice);
        }
        return BigDecimal.ZERO;
    }

    // 计算折扣率（百分比）
    public BigDecimal getDiscountRate() {
        if (originalPrice != null && currentPrice != null
                && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = originalPrice.subtract(currentPrice);
            return discount.divide(originalPrice, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    // 判断是否有折扣
    public boolean hasDiscount() {
        return getDiscount().compareTo(BigDecimal.ZERO) > 0;
    }
}