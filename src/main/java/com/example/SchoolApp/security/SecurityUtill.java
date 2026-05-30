package com.example.SchoolApp.security;

import com.example.SchoolApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collection;

public class SecurityUtill {

    @Autowired
    private UserService userService;

    public static String getSessionLoader(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static Collection<? extends  GrantedAuthority> getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities();
    }
    public static boolean hasRole(String role){
        Collection<? extends GrantedAuthority> authorities = getRole();
        return authorities.contains(new SimpleGrantedAuthority(role));
    }

}
