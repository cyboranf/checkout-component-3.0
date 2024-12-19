package com.component.checkout.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Configures the main security filter chain for HTTP requests.
     *
     * @param http the HttpSecurity configuration.
     * @return a configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring Security Filter Chain");
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(this::createCorsConfiguration))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/cart/add-items").hasAuthority("ROLE_CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/cart/checkout").hasAuthority("ROLE_CLIENT")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            LOGGER.warn("Unauthorized access attempt: {}", authException.getMessage());
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"Unauthorized access: " + authException.getMessage() + "\"}"
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            LOGGER.warn("Access denied: {}", accessDeniedException.getMessage());
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"Access denied: " + accessDeniedException.getMessage() + "\"}"
                            );
                        })
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Returns a PasswordEncoder bean for hashing passwords.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        LOGGER.info("Initializing PasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    /**
     * Returns an AuthenticationManager instance from the provided AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the configuration containing authentication details.
     * @return an AuthenticationManager.
     * @throws Exception if unable to retrieve AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        LOGGER.info("Initializing AuthenticationManager bean");
        return authenticationConfiguration.getAuthenticationManager();
    }

    private org.springframework.web.cors.CorsConfiguration createCorsConfiguration(HttpServletRequest request) {
        LOGGER.info("Creating CORS configuration");
        var corsConfig = new org.springframework.web.cors.CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        return corsConfig;
    }
}
