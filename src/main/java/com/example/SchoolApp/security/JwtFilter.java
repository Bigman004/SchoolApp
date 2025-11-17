package com.example.SchoolApp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.NativeDetector.Context;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.SchoolApp.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter {
	
	JWTService jwt;
	private ApplicationContext context;
	
	@Autowired
	public JwtFilter(JWTService jwt, ApplicationContext context) {
		this.jwt = jwt;
		this.context =context;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		// Skip filter for login endpoint
		if(request.getRequestURI().equals("/login")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// Extract token from Bearer header
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // Remove "Bearer " prefix
			
			try {
				username = jwt.extractUsername(token);
			} catch(Exception e) {
				System.out.println("Invalid JWT: " + e.getMessage());
			}
		}
		
		// Authenticate if username is extracted and no existing authentication
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = context.getBean(CustomUserDetailService.class).loadUserByUsername(username);
			
			if(jwt.validateToken(token, user)) {
				UsernamePasswordAuthenticationToken authToken 
					= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);	
	}

}
