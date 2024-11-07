package com.school.SchoolManagement.Config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.SchoolManagement.JWT.AuthTokenFilter;
import com.school.SchoolManagement.Service.AuthService.UserPrinciple;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private AuthEntryPointConfig authEntryPointConfig;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserPrinciple();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized");
            body.put("message", "Authentication is required to access this resource.");
            body.put("path", request.getServletPath());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/login", "/api/user/register", "/swagger", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll()
                                .requestMatchers("/api/schoolmanagement/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER", "PARENT", "LIBRARIAN")
//                                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN","STUDENT")
//                                .requestMatchers("/api/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.authenticationEntryPoint(authEntryPointConfig))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .authenticationProvider(authenticationProvider()) // Custom authentication provider
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }


}
