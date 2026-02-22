package com.form.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/cookies/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/preventas/activa").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/clientes/buscar").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/cookies/upload").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/packs", "/api/packs/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/pedidos", "/api/pedidos/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/pedidos", "/api/pedidos/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/pedidos/**").authenticated()

                                .requestMatchers("/api/egresos", "/api/egresos/**").authenticated()

                                .requestMatchers("/api/preventas", "/api/preventas/**").authenticated()

                                .requestMatchers("/api/clientes", "/api/clientes/**").authenticated()

                                .requestMatchers("/api/cookies", "/api/cookies/**").authenticated()
                                .requestMatchers("/api/packs", "/api/packs/**").authenticated()

                                .requestMatchers("/api/stats", "/api/stats/**").authenticated()

                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://flavis-cookies.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("User not found");
        };
    }
}