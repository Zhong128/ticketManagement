package org.example.ticketmanagement.pojo; // 请确保包名与你的项目一致

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategory {
    private Long id;             // 分类ID，主键
    private String name;         // 分类名称（如：演唱会、话剧歌剧）
    private Integer sortOrder;   // 排序字段，数字越小越靠前
    private Integer status;      // 状态：0-禁用，1-启用
    private LocalDateTime createTime; // 创建时间
}