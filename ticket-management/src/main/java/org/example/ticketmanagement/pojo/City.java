// org/example/ticketmanagement/pojo/City.java
package org.example.ticketmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private Long id;            // 城市ID，主键
    private String name;        // 城市名称（如：北京、上海）
    private String code;        // 城市代码（如：BEIJING、SHANGHAI）
    private String province;    // 省份/地区（如：北京市、上海市）
    private Integer hotLevel;   // 热门等级：0-普通，1-热门
    private Integer status;     // 状态：0-禁用，1-启用
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime; // 更新时间
}