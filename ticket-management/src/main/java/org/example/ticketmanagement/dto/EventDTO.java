// org/example/ticketmanagement/dto/EventDTO.java
package org.example.ticketmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    @NotBlank(message = "演出名称不能为空")
    @Size(max = 200, message = "演出名称不能超过200个字符")
    private String name;

    @Size(max = 100, message = "艺人名称不能超过100个字符")
    private String artistName;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "城市ID不能为空")
    private Long cityId;

    @NotBlank(message = "场馆不能为空")
    @Size(max = 200, message = "场馆名称不能超过200个字符")
    private String venue;

    @Size(max = 500, message = "封面图URL不能超过500个字符")
    private String coverImage;

    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "开售时间不能为空")
    private LocalDateTime saleStartTime;

    @NotNull(message = "停售时间不能为空")
    private LocalDateTime saleEndTime;

    @NotNull(message = "演出开始时间不能为空")
    private LocalDateTime eventStartTime;

    @NotNull(message = "演出结束时间不能为空")
    private LocalDateTime eventEndTime;
}