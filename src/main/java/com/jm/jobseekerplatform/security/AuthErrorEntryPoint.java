package com.jm.jobseekerplatform.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public final class AuthErrorEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthErrorEntryPoint.class);

    private static final String JSON_HEADER_VALUE = "application/json";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        logger.debug("Request URI: {}", request.getRequestURI());
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (request.getRequestURI().contains("/api/") ||
                (contentType != null && contentType.contains(JSON_HEADER_VALUE))) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } else {
            response.sendRedirect("/login");
        }
    }

}
