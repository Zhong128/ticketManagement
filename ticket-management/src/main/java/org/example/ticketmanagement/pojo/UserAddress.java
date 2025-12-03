package org.example.ticketmanagement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    private Long id;
    private Long userId;
    @Size(max = 10, message = "真实姓名长度不能超过10个字符")
    private String receiverName;
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确，应为6位数字")
    private String postalCode;
    @JsonIgnore
    private LocalDateTime createTime;
    @JsonIgnore
    private LocalDateTime updateTime;
}
