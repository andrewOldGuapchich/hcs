package com.andrew.hcsservice.config.filter;

import com.andrew.hcsservice.service.auth_service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        try{
            UserDetails regUser = authService.loadUserByUsername(token);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            regUser.getUsername(), null, regUser.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e){
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
