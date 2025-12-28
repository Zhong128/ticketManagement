package org.example.ticketmanagement.mapper.provider;

import org.apache.ibatis.jdbc.SQL;
import org.example.ticketmanagement.dto.EventCategoryQueryDTO;

/**
 * 分类动态SQL提供者
 */
public class EventCategorySqlProvider {

    /**
     * 构建动态查询SQL
     */
    public String selectByCondition(EventCategoryQueryDTO queryDTO) {
        return new SQL() {{
            SELECT("id, name, sort_order, status, create_time");
            FROM("event_category");

            // 动态添加WHERE条件
            buildWhereClause(this, queryDTO);

            // 排序
            if (queryDTO.getSortBy() != null && queryDTO.getSortOrder() != null) {
                ORDER_BY(queryDTO.getOrderByClause());
            } else {
                ORDER_BY("sort_order ASC, create_time DESC");
            }

            // 分页
            LIMIT("#{size}");
            OFFSET("#{offset}");
        }}.toString();
    }

    /**
     * 构建计数SQL
     */
    public String countByCondition(EventCategoryQueryDTO queryDTO) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("event_category");

            // 动态添加WHERE条件
            buildWhereClause(this, queryDTO);
        }}.toString();
    }

    /**
     * 构建WHERE条件（公共逻辑）
     */
    private void buildWhereClause(SQL sql, EventCategoryQueryDTO queryDTO) {
        if (queryDTO.getStatus() != null) {
            sql.WHERE("status = #{status}");
        }

        if (queryDTO.needNameLike()) {
            sql.WHERE("name LIKE CONCAT('%', #{name}, '%')");
        }

        if (queryDTO.needSortRange()) {
            sql.WHERE("sort_order BETWEEN #{minSort} AND #{maxSort}");
        }
    }
}