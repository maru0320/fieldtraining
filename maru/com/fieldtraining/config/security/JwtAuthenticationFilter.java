package com.fieldtraining.config.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fieldtraining.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                     HttpServletResponse servletResponse,
                                     FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        LOGGER.info("[doFilterInternal] token 값 추출 체크 완료. token: {}", token);

        if (token != null && jwtTokenProvider.validateToken(token) && !tokenBlacklistService.isTokenBlacklisted(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            LOGGER.info("[authentication] 객체 여부 확인: {}", authentication);
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
            // 인증 정보가 유효하다면 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("Authentication 설정: {}", SecurityContextHolder.getContext().getAuthentication());
        }

        // 필터 체인의 다음 단계로 전달
        filterChain.doFilter(servletRequest, servletResponse);
    }
}