package org.example.ticketmanagement.vo;

import lombok.Data;
import java.util.List;

/**
 * 分类及其对应演出的VO
 */
@Data
public class CategoryWithEventsVO {
    private EventCategoryVO category;      // 分类信息
    private List<EventVO> events;          // 该分类下的演出列表
    private Integer eventCount;            // 演出数量
    private Boolean hasMore;               // 是否有更多

    public CategoryWithEventsVO(EventCategoryVO category, List<EventVO> events) {
        this.category = category;
        this.events = events;
        this.eventCount = events != null ? events.size() : 0;
        this.hasMore = this.eventCount > 0 && this.eventCount >= 4; // 假设每类最多显示4条
    }
}