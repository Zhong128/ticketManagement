// org/example/ticketmanagement/dto/CityQueryDTO.java
package org.example.ticketmanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ticketmanagement.pojo.PageQuery;

/**
 * 城市查询参数DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CityQueryDTO extends PageQuery {
    private String name;         // 城市名称（模糊查询）
    private String code;         // 城市代码
    private String province;     // 省份
    private Integer status;      // 状态：0-禁用，1-启用
    private Integer hotLevel;    // 热门等级：0-普通，1-热门

    /**
     * 是否需要根据名称模糊查询
     */
    public boolean needNameLike() {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * 是否需要根据代码精确查询
     */
    public boolean needCodeExact() {
        return code != null && !code.trim().isEmpty();
    }

    /**
     * 是否需要根据省份查询
     */
    public boolean needProvince() {
        return province != null && !province.trim().isEmpty();
    }
}