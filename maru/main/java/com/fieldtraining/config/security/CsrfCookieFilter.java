package com.fieldtraining.config.security;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        if (csrfToken != null && csrfToken.getHeaderName() != null) {
            log.info("csrfToken.getHeaderName() = {}, csrfToken.getToken() = {}", csrfToken.getHeaderName(), csrfToken.getToken());
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        } else {
            log.warn("CSRF Token is missing or not set properly.");
        }
        
        filterChain.doFilter(request, response);
    }
}