package com.company.security.jwt;

import com.company.dto.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.company.constant.SecurityConstants.ACCESS_DENIED_MESSAGE;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        HttpResponse httpResponse = new HttpResponse(
                httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                ACCESS_DENIED_MESSAGE,
                new Date());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        ServletOutputStream outputStream = response.getOutputStream();
        new ObjectMapper().writeValue(outputStream, httpResponse);
        outputStream.flush();
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied");
    }
}
