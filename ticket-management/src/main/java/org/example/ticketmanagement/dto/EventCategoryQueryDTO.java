// org/example/ticketmanagement/dto/EventCategoryQueryDTO.java
package org.example.ticketmanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ticketmanagement.pojo.PageQuery;

/**
 * 分类查询参数DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventCategoryQueryDTO extends PageQuery {
    private String name;         // 分类名称（模糊查询）
    private Integer status;      // 状态：0-禁用，1-启用
    private Integer minSort;     // 最小排序值
    private Integer maxSort;     // 最大排序值

    /**
     * 是否需要根据名称模糊查询
     */
    public boolean needNameLike() {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * 是否需要根据排序范围查询
     */
    public boolean needSortRange() {
        return minSort != null && maxSort != null;
    }
}