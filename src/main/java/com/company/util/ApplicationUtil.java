package com.company.util;

import com.company.dto.HttpResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ApplicationUtil {
    private ApplicationUtil(){}

    public static HttpResponse getHttpResponseObject(HttpStatus httpStatus, String message) {
        return HttpResponse.builder()
                .httpStatus(httpStatus).message(message)
                .reason(httpStatus.getReasonPhrase().toUpperCase()).timeStamp(new Date())
                .httpStatusCode(httpStatus.value()).build();
    }

}
