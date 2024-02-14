package com.cookbook.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//used to commence the authentication process when an unauthenticated request is encountered
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            //HTTP request made by client
            HttpServletRequest request,
            //HTTP response sent back to client
            HttpServletResponse response,
            //Authentication exception that occurred
            AuthenticationException authException)
            throws IOException, ServletException {

        //HTTP 401 response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
