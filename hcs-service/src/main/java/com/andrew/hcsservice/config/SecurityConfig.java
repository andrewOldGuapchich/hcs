package com.andrew.hcsservice.config;

import com.andrew.hcsservice.config.filter.JwtFilter;
import com.andrew.hcsservice.service.auth_service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final AuthService authService;
    @Autowired
    public SecurityConfig(AuthService authService){
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .cors().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/internal/**").hasAuthority("WORKER")
                .antMatchers("/flat/**").hasAuthority("ADMIN")
                .antMatchers("/doc/**").permitAll()
                .antMatchers("/user/**").hasAuthority("USER")
                .anyRequest().denyAll()
                .and()
                .userDetailsService(authService)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return authService;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(authService);
    }
}
