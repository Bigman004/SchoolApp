package com.example.SchoolApp.security;

import com.example.SchoolApp.events.CreateLogEvent;
import com.example.SchoolApp.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class MonitoringFilter extends OncePerRequestFilter {

    ApplicationEventPublisher publisher;
    private JWTService jwtService;

    @Autowired
    public MonitoringFilter(ApplicationEventPublisher publisher,  JWTService jwtService) {
        this.publisher = publisher;
        this.jwtService = jwtService;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
            boolean success = false;
            chain.doFilter(request, response);
            String username = "";
            if(containsAuthentication(request) && !request.getRequestURI().equals("/login")) {
                String token = request.getHeader("Authorization").substring(7);
                username = jwtService.extractUsername(token);
            }
            if(response.getStatus() >= 200 && response.getStatus() < 300) {
                success = true;
            }

            publisher.publishEvent(new CreateLogEvent(
                    username,
                    request.getRequestURI(),
                    containsAuthentication(request),
                    "body",
                    response.getStatus(),
                    request.getMethod(),
                    request.getRemoteAddr(),
                    request.getRemoteHost(), String.valueOf(request.getRemotePort()),
                    response.getHeader("Access-Control-Allow-Origin"),
                    "body", success

                    ));
    }
    public boolean containsAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}
