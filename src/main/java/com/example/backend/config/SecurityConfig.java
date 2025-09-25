package com.example.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.authentication.JwtAuthenticationFilter;
import com.example.backend.authentication.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // HTTP 기본 인증 및 CSRF 보안 비활성화
            .httpBasic(httpBasic -> httpBasic.disable())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 세션 관리 정책을 STATELESS(상태 비저장)로 설정
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 요청별 권한 설정
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/user/**").permitAll()
                    .requestMatchers("/business/**").hasAnyRole("BUSINESS")
                    .requestMatchers("/api/business/**").hasAnyRole("BUSINESS")
            		.requestMatchers("/api/notices/**").hasAnyRole("ADMIN_LEVEL1")
            		.requestMatchers("/api/admin/business-users").hasAnyRole("ADMIN_LEVEL1")
            		)
            
            // 이전에 만든 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
        		"http://localhost:7000", 
                "http://127.0.0.1:7000",
                "http://localhost:5174",
                "http://127.0.0.1:5174",
                "http://localhost:6500"
        		));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        // 최고 관리자 > 중간 관리자 > 하위 관리자 > 사업자 > 일반 사용자 순서로 권한 설정
        hierarchy.setHierarchy(
            "ROLE_ADMIN_SUPER > ROLE_ADMIN_LEVEL2\n" +
            "ROLE_ADMIN_LEVEL2 > ROLE_ADMIN_LEVEL1\n"
        );
        return hierarchy;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 암호화 알고리즘을 사용하는 PasswordEncoder를 반환
        return new BCryptPasswordEncoder();
    }
}
