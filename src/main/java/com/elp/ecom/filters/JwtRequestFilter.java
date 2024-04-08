package com.elp.ecom.filters;

import com.elp.ecom.services.jwt.UserDetailsServiceImpl;
import com.elp.ecom.utils.JwtUtl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtl jwtUtl;

    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService, JwtUtl jwtUtl) {
        this.userDetailsService = userDetailsService;
        this.jwtUtl = jwtUtl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader =request.getHeader("Authorization");
        String token = null;
        String username =null;
//validation
        if (authHeader!=null && authHeader.startsWith("Bearer")){
            token =authHeader.substring(7);
            username = jwtUtl.extractUserName(token);
        }
        //validate user
if(username != null && SecurityContextHolder.getContext().getAuthentication()==null){
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    //validate token
        if(jwtUtl.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        }
filterChain.doFilter(request, response);
    }
}
