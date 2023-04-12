package com.company.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
