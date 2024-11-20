package com.packt.fieldtraining.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {

	private final JwtTokenProvider jwtTokenProvider;

	@Autowired
	public SecurityConfiguration(JwtTokenProvider jwtTokenProvider){
		this.jwtTokenProvider = jwtTokenProvider;
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
			return httpSecurity
					
					.cors().and()
			        .csrf().disable()  // CORS와 CSRF 관련 문제를 피하기 위해 비활성화 (리액트와 연동시 필요할 수 있음)
			        .authorizeRequests()
			            .requestMatchers("/login").authenticated()  // API 엔드포인트는 인증 필요
			            .requestMatchers("/").access("hasRole('ROLE_ADMIN')")
			            .anyRequest().permitAll()  // 그 외 모든 요청은 허용
			        .and()
			        .formLogin()
			            .loginPage("http://localhost:3000") // 리액트 로그인 페이지로 리다이렉트
			            .permitAll()
			        .and()
			        .build();

					
					
//					.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//						@Override
//						public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//	                        CorsConfiguration config = new CorsConfiguration();
//	                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000/"));
//	                        config.setAllowedMethods(Collections.singletonList("*"));
//	                        config.setAllowCredentials(true);
//	                        config.setAllowedHeaders(Collections.singletonList("*"));
//	                        config.setMaxAge(3600L); //1시간
//	                        return config;
//						}
//					}))
//					 .csrf(csrf -> csrf
//				     //.csrfTokenRequestHandler(requestHandler)
//				     .ignoringRequestMatchers("/login", "/generalJoin")
//				     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//				     /* BasicAuthenticationFilter 이후에 CsrfCookieFilter를 실행한다.
//				                   BasicAuthenticationFilter는 로그인 이후에 동작하는 필터*/
//				     )
			
//					.authorizeHttpRequests(request -> request
//					.requestMatchers("/").hasRole("USER")
//					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//					)
			
//					  .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//				      .authorizeHttpRequests((requests) -> requests
//				      .requestMatchers("/myHome", "/generalJoin", "/user").authenticated()
//				      .requestMatchers("/login").permitAll())
//				      .formLogin(form -> form
//				    		  .loginPage("/login")
//				    		  .permitAll()
//				    		  )
//				      .httpBasic(Customizer.withDefaults())
//				                
//				      .exceptionHandling(exceptionHandling -> exceptionHandling
//				    		  .accessDeniedHandler(new CustomAccessDeniedHandler())
//				    		  .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
//				     
//				                
//				                .build();
		
	}
	
}
