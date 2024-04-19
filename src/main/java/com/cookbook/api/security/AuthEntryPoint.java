//package com.cookbook.api.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class AuthEntryPoint implements AuthenticationEntryPoint {
//
//    private final RequestMatcher requestMatcher;
//
//    @Value("${security.redirect.login")
//    private String loginUrl;
//
//    public AuthEntryPoint() {
//        this.requestMatcher = new AntPathRequestMatcher("/login");
//    }
//
//    @Override
//    public void commence(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException authException) throws IOException, ServletException {
//        if(requestMatcher.matches(request)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.sendRedirect(loginUrl);
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//            response.getWriter().write("Unauthorized");
//        }
//
//
//
//    }
//}
