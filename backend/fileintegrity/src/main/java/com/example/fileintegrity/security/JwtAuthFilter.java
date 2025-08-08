package com.example.fileintegrity.security;

import com.example.fileintegrity.service.UserService;
import com.example.fileintegrity.util.JwtService;
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
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException {
        try{
            String token = null;
            String userName = null;
            if(request.getCookies() != null){
                for(var cookie : request.getCookies()){
                    if(cookie.getName().equals("jwt")){
                        token = cookie.getValue();
                        userName = jwtService.extractUser(token);
                        break;
                    }
                }
            }
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

                    UserDetails user = userService.loadUserByUsername(userName);
                    if (Boolean.TRUE.equals(jwtService.validateToken(token, user))) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}













