// org/example/ticketmanagement/dto/EventSessionDTO.java
package org.example.ticketmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSessionDTO {
    @NotNull(message = "演出ID不能为空")
    private Long eventId;

    @NotBlank(message = "场次名称不能为空")
    @Size(max = 100, message = "场次名称不能超过100个字符")
    private String sessionName;

    @NotNull(message = "场次时间不能为空")
    private LocalDateTime sessionTime;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态必须为0或1")
    @Max(value = 1, message = "状态必须为0或1")
    private Integer status;
}