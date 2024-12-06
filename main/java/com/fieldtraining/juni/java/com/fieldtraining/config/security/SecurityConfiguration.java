package com.fieldtraining.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fieldtraining.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenBlacklistService tokenBlacklistService;
	
	@Autowired
	public SecurityConfiguration(JwtTokenProvider jwtTokenProvider, TokenBlacklistService tokenBlacklistService){
		this.jwtTokenProvider = jwtTokenProvider;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
			return httpSecurity
					
					.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
						@Override
						public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
							CorsConfiguration config = new CorsConfiguration();
							
							config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
							config.setAllowedMethods(Collections.singletonList("*"));
							config.setAllowCredentials(true);
							config.setAllowedHeaders(Collections.singletonList("*"));
							config.setMaxAge(3600L);
							return config;
						}
					}))
			        .csrf().disable()  // CORS와 CSRF 관련 문제를 피하기 위해 비활성화 (리액트와 연동시 필요할 수 있음)
			        .sessionManagement(sessionManagement -> sessionManagement
			        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT 방식으로 세션 사용안함 
			        .authorizeRequests()
			            .requestMatchers("/login", "/generaljoin", "/logout", "/", "/institutionaljoin").permitAll()  // API 엔드포인트는 인증 필요
			            .requestMatchers("/practice").access("hasRole('STUDENT')")
			            .anyRequest().permitAll()  // 그 외 모든 요청은 허용
			        .and()
			        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenBlacklistService), UsernamePasswordAuthenticationFilter.class)
			        .formLogin()
			            .loginPage("http://localhost:3000")
			            .permitAll()
			        .and()
			        .logout(logout -> logout
			                .logoutUrl("/logout")
			                .logoutSuccessHandler(customLogoutSuccessHandler())) // 커스텀 핸들러
			        .build();

		
	}
	
	private LogoutSuccessHandler customLogoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Logout successful\"}");
        };
    }
	
}