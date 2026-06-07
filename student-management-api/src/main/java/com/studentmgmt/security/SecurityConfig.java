package com.studentmgmt.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(encoder.encode(adminPassword))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
    	.csrf(AbstractHttpConfigurer::disable)
    	.sessionManagement(session ->
    	        session.sessionCreationPolicy(
    	                SessionCreationPolicy.STATELESS))
    	.authorizeHttpRequests(auth -> auth
    	        .requestMatchers("/auth/login").permitAll()
    	        .requestMatchers("/swagger-ui.html").permitAll()
    	        .requestMatchers("/swagger-ui/**").permitAll()
    	        .requestMatchers("/v3/api-docs/**").permitAll()
    	        .requestMatchers("/h2-console/**").permitAll()
    	        .anyRequest().authenticated())
    	.addFilterBefore(
    	        jwtFilter,
    	        UsernamePasswordAuthenticationFilter.class)
    	.headers(headers -> headers.frameOptions(frame -> frame.disable())); // for H2 console

        return http.build();
    }
}
