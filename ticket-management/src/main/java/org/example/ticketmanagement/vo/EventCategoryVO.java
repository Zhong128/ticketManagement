package org.example.ticketmanagement.vo; // 可以放在dto包，也可以新建vo包，按你习惯

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryVO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}