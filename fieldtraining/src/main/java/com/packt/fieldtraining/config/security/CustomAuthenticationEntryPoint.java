package com.packt.fieldtraining.config.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packt.fieldtraining.data.dto.EntryPointErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
    
    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 인증 실패 이유 로그에 남기기
        LOGGER.error("[commence] 인증 실패로 response.sendError 발생. 에러 메시지: {}", e.getMessage());

        EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();

        // AuthenticationException에서 메시지를 받아오거나, 구체적인 오류에 맞는 메시지 설정
        String errorMessage = e.getMessage() != null ? e.getMessage() : "인증이 실패했습니다.";

        entryPointErrorResponse.setMsg(errorMessage);

        // 응답 상태 코드 401 설정 (인증 실패)
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");

        // JSON 응답으로 에러 메시지 전달
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
    }
}
