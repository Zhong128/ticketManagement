// org/example/ticketmanagement/dto/CityVO.java
package org.example.ticketmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityVO {
    private Long id;
    private String name;
    private String code;
    private String province;
    private Integer hotLevel;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}