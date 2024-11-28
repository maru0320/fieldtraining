package com.packt.fieldtraining.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // "/api/**" 경로에 대해서만 CORS 설정을 적용
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")  // React 앱이 실행되는 주소 (기본적으로 React는 3000번 포트를 사용)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");  // 모든 헤더 허용
    }
}
