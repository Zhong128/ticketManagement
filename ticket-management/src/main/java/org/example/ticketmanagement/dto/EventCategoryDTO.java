package org.example.ticketmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryDTO {

    @NotBlank(message = "分类名称不能为空")
    private String name;         // 分类名称

    @NotNull(message = "排序值不能为空")
    private Integer sortOrder;   // 排序值

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值必须为0或1")
    @Max(value = 1, message = "状态值必须为0或1")
    private Integer status;      // 状态：0-禁用，1-启用
}