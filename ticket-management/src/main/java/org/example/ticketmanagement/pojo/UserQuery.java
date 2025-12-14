// org/example/ticketmanagement/pojo/UserQuery.java
package org.example.ticketmanagement.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends PageQuery {
    private String username;   // 用户名模糊查询
    private String email;      // 邮箱模糊查询
    private String phone;      // 手机号精确查询
    private String nickname;   // 昵称模糊查询
    private String realName;   // 真实姓名模糊查询
    private Integer status;    // 状态：0-禁用，1-启用
    private String role;       // 角色：USER, ADMIN
    private LocalDate birthdayFrom;  // 生日范围查询开始
    private LocalDate birthdayTo;    // 生日范围查询结束
    private LocalDateTime createTimeFrom;  // 创建时间范围开始
    private LocalDateTime createTimeTo;    // 创建时间范围结束

    /**
     * 检查是否有查询条件
     */
    public boolean hasQueryConditions() {
        return username != null || email != null || phone != null ||
                nickname != null || realName != null || status != null ||
                role != null || birthdayFrom != null || birthdayTo != null ||
                createTimeFrom != null || createTimeTo != null;
    }

    /**
     * 获取生日范围SQL条件
     */
    public String getBirthdayCondition() {
        if (birthdayFrom != null && birthdayTo != null) {
            return " AND birthday BETWEEN #{birthdayFrom} AND #{birthdayTo}";
        } else if (birthdayFrom != null) {
            return " AND birthday >= #{birthdayFrom}";
        } else if (birthdayTo != null) {
            return " AND birthday <= #{birthdayTo}";
        }
        return "";
    }

    /**
     * 获取创建时间范围SQL条件
     */
    public String getCreateTimeCondition() {
        if (createTimeFrom != null && createTimeTo != null) {
            return " AND create_time BETWEEN #{createTimeFrom} AND #{createTimeTo}";
        } else if (createTimeFrom != null) {
            return " AND create_time >= #{createTimeFrom}";
        } else if (createTimeTo != null) {
            return " AND create_time <= #{createTimeTo}";
        }
        return "";
    }
}