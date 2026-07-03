package com.project.AppraisalSystem.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // ← THIS LINE — uses your CorsConfig bean
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Public ──────────────────────────────────────────────
                        .requestMatchers("/api/users/login").permitAll()

                        // ── Any authenticated user can view a user profile by id ──
                        .requestMatchers(HttpMethod.GET, "/api/users/*").authenticated()

                        // ── Manager can view their own team ───────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/users/manager/**").hasAnyRole("MANAGER", "HR")

                        // ── HR only ─────────────────────────────────────────────
                        .requestMatchers("/api/users/**").hasRole("HR")
                        .requestMatchers("/api/departments/**").hasRole("HR")
                        .requestMatchers(HttpMethod.POST,   "/api/appraisals").hasRole("HR")
                        .requestMatchers(HttpMethod.DELETE, "/api/appraisals/**").hasRole("HR")
                        .requestMatchers(HttpMethod.PATCH,  "/api/appraisals/*/approve").hasRole("HR")

                        // ── Manager only ─────────────────────────────────────────
                        .requestMatchers("/api/appraisals/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/appraisals/*/manager-review/**").hasAnyRole("MANAGER")

                        // ── Employee + Manager ───────────────────────────────────
                        .requestMatchers("/api/appraisals/employee/**").hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/appraisals/*/self-assessment/**").hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/appraisals/*/employee-view").hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/appraisals/*/acknowledge").hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/goals/**").hasAnyRole("EMPLOYEE", "MANAGER")

                        // ── All authenticated users ──────────────────────────────
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/api/appraisals/**").authenticated()

                        // ── Everything else requires authentication ───────────────
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}