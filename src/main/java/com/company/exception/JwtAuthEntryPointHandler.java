package com.company.exception;

import com.company.dto.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.company.constant.SecurityConstants.*;
@Component
public class JwtAuthEntryPointHandler extends Http403ForbiddenEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        HttpResponse httpResponse = new HttpResponse(
                httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                FORBIDDEN_MESSAGE,
                new Date());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        ServletOutputStream outputStream = response.getOutputStream();
        new ObjectMapper().writeValue(outputStream, httpResponse);
        outputStream.flush();
       // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
    }
}
