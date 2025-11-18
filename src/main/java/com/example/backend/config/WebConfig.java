package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir-default}")
    private String defaultUploadDir;

    @Value("${file.upload-dir-alt}")
    private String altUploadDir;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5174", // Vite 기본 포트
                    "http://localhost:8080", // Vue CLI 기본 포트
                    "http://localhost:8889",
                    "http://localhost:7000",// 관리자 페이지 포트
                    "http://localhost:6500",// 사업자 페이지 포트
                    "https://www.hotelhub.store"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String locDefault = "file:" + defaultUploadDir + "/";
        String locAlt = "file:" + altUploadDir + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(locDefault, locAlt);
    }
}