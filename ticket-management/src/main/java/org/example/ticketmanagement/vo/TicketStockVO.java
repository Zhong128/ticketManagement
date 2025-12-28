// org/example/ticketmanagement/vo/TicketStockVO.java
package org.example.ticketmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStockVO {
    private Long eventId;
    private Long sessionId;
    private boolean hasStock;  // 是否有库存
    private boolean onSale;    // 是否已开票
    private List<Map<String, Object>> tierStockList; // 各票档库存详情

    // 静态工厂方法
    public static TicketStockVO create(Long eventId, Long sessionId, boolean hasStock, boolean onSale) {
        TicketStockVO vo = new TicketStockVO();
        vo.setEventId(eventId);
        vo.setSessionId(sessionId);
        vo.setHasStock(hasStock);
        vo.setOnSale(onSale);
        return vo;
    }
}