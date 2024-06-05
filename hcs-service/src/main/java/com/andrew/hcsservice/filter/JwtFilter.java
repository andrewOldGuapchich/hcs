package com.andrew.hcsservice.filter;

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
        System.out.println("Jwt filter");
        String token = request.getHeader("Authorization");
        try{
            UserDetails regUser = authService.loadUserByUsername(token);
            System.out.println("get regUser");
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            regUser.getUsername(), null, regUser.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        } catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
        System.out.println("Filter work " + request.getMethod() + " " + response.getStatus());
    }
}
