// org/example/ticketmanagement/vo/StockStatisticsVO.java
package org.example.ticketmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockStatisticsVO {
    // 查询条件
    private Long eventId;
    private Long sessionId;
    private Long tierId;

    // 总体统计
    private Integer totalTiers;           // 票档总数
    private Integer activeTiers;          // 启用中的票档数
    private Integer totalStock;           // 总库存
    private Integer totalSold;            // 总已售
    private Integer totalAvailable;       // 总可用
    private BigDecimal overallSellRate;   // 总体销售率

    // 票档详情列表
    private List<TierStockDetail> tierDetails;

    // 统计时间
    private String statisticsTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TierStockDetail {
        private Long tierId;
        private String tierName;
        private Integer totalStock;
        private Integer soldStock;
        private Integer availableStock;
        private BigDecimal sellRate;
        private BigDecimal price;
        private Integer status;           // 0-禁用，1-启用
        private String lastUpdateTime;
    }
}
