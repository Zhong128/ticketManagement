// org/example/ticketmanagement/dto/CaptchaResult.java
package org.example.ticketmanagement.dto;

import lombok.Data;

@Data
public class CaptchaResult {
    private String captchaKey;
    private String imageBase64;
}
