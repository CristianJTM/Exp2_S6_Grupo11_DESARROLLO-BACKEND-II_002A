package com.minimarket.security.config;

import com.minimarket.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/auth/**",
                                "/public/**",
                                "/h2-console/**")
                        .permitAll()

                        .requestMatchers("/api/usuarios/**")
                        .hasAuthority("ADMINISTRADOR")

                        .requestMatchers("/api/categorias/**")
                        .hasAuthority("ADMINISTRADOR")

                        .requestMatchers("/api/productos/**")
                        .hasAnyAuthority(
                                "EMPLEADO",
                                "ADMINISTRADOR")

                        .requestMatchers("/api/inventario/**")
                        .hasAnyAuthority(
                                "EMPLEADO",
                                "ADMINISTRADOR")

                        .requestMatchers("/api/ventas/**")
                        .hasAnyAuthority(
                                "EMPLEADO",
                                "ADMINISTRADOR")

                        .requestMatchers("/api/detalle-ventas/**")
                        .hasAnyAuthority(
                                "EMPLEADO",
                                "ADMINISTRADOR")

                        .requestMatchers("/api/carrito/**")
                        .hasAnyAuthority(
                                "CLIENTE",
                                "EMPLEADO",
                                "ADMINISTRADOR")

                        .anyRequest()
                        .authenticated()
                )

                .headers(headers ->
                        headers.frameOptions(
                                frame -> frame.disable()))

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}