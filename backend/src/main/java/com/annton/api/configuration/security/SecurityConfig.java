package com.annton.api.configuration.security;

import com.annton.api.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private CorsConfigurationSource configurationSource;
    public static final List<String> openUris = List.of("/api/v1/auth/**"
            , "/api/v1/info/**", "/static/**", "/img/**", "**.html");
    public static Map<String, HttpMethod> urlMethodOpen = new HashMap<>();

    static {
        urlMethodOpen.put("/api/v1/applications", HttpMethod.POST);
    }

    @Autowired
    @Qualifier("allowAll")
    public void setConfigurationSource(CorsConfigurationSource configurationSource) {
        this.configurationSource = configurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> cors
                        .configurationSource(configurationSource)
                ).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    openUris.forEach(url -> authorizeRequests.requestMatchers(url).permitAll());
                    urlMethodOpen.forEach((url, method) -> authorizeRequests.requestMatchers(method, url).permitAll());
                    authorizeRequests.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}