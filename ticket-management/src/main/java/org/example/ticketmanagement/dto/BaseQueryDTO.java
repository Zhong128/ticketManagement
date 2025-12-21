// org/example/ticketmanagement/pojo/BaseQueryDTO.java
package org.example.ticketmanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ticketmanagement.pojo.PageQuery;

/**
 * 基础查询DTO（包含分页参数）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseQueryDTO extends PageQuery {
    // 可以在这里添加公共查询参数
    // 例如：keyword（关键词搜索）
}