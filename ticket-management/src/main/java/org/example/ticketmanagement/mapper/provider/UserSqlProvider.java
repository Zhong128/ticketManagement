// org/example/ticketmanagement/mapper/provider/UserSqlProvider.java
package org.example.ticketmanagement.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.example.ticketmanagement.pojo.UserQuery;

public class UserSqlProvider {

    /**
     * 构建分页查询SQL
     */
    public String selectUsersByPage(UserQuery query) {
        return new SQL() {{
            SELECT("*");
            FROM("user");

            // 添加查询条件
            buildConditions(this, query);

            // 排序
            if (query.getSortBy() != null && query.getSortOrder() != null) {
                String orderByClause = query.getOrderByClause();
                ORDER_BY(orderByClause);
            } else {
                ORDER_BY("create_time DESC");
            }

            // 分页
            if (query.validate()) {
                LIMIT("#{size} OFFSET #{offset}");
            }
        }}.toString();
    }

    /**
     * 构建统计数量SQL
     */
    public String countUsers(UserQuery query) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("user");
            buildConditions(this, query);
        }}.toString();
    }

    /**
     * 构建查询条件
     */
    private void buildConditions(SQL sql, UserQuery query) {
        sql.WHERE("1=1");

        if (query.getUsername() != null && !query.getUsername().trim().isEmpty()) {
            sql.WHERE("username LIKE CONCAT('%', #{username}, '%')");
        }
        if (query.getEmail() != null && !query.getEmail().trim().isEmpty()) {
            sql.WHERE("email LIKE CONCAT('%', #{email}, '%')");
        }
        if (query.getPhone() != null && !query.getPhone().trim().isEmpty()) {
            sql.WHERE("phone = #{phone}");
        }
        if (query.getNickname() != null && !query.getNickname().trim().isEmpty()) {
            sql.WHERE("nickname LIKE CONCAT('%', #{nickname}, '%')");
        }
        if (query.getRealName() != null && !query.getRealName().trim().isEmpty()) {
            sql.WHERE("real_name LIKE CONCAT('%', #{realName}, '%')");
        }
        if (query.getStatus() != null) {
            sql.WHERE("status = #{status}");
        }
        if (query.getRole() != null && !query.getRole().trim().isEmpty()) {
            sql.WHERE("role = #{role}");
        }

        // 生日范围查询
        if (query.getBirthdayFrom() != null && query.getBirthdayTo() != null) {
            sql.WHERE("birthday BETWEEN #{birthdayFrom} AND #{birthdayTo}");
        } else if (query.getBirthdayFrom() != null) {
            sql.WHERE("birthday >= #{birthdayFrom}");
        } else if (query.getBirthdayTo() != null) {
            sql.WHERE("birthday <= #{birthdayTo}");
        }

        // 创建时间范围查询
        if (query.getCreateTimeFrom() != null && query.getCreateTimeTo() != null) {
            sql.WHERE("create_time BETWEEN #{createTimeFrom} AND #{createTimeTo}");
        } else if (query.getCreateTimeFrom() != null) {
            sql.WHERE("create_time >= #{createTimeFrom}");
        } else if (query.getCreateTimeTo() != null) {
            sql.WHERE("create_time <= #{createTimeTo}");
        }
    }
}