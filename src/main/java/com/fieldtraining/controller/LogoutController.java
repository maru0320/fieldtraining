package com.fieldtraining.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.config.security.JwtTokenProvider;
import com.fieldtraining.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@RestController
@RequestMapping("/api/auth") // API 경로를 지정
@CrossOrigin(origins = "http://localhost:3000") // React 클라이언트 허용
public class LogoutController {
	
	@Value("${springboot.jwt.secret}")
	private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        LocalDateTime expiresAt = extractTokenExpiration(jwtToken);

        logger.info("로그아웃 요청: 토큰 = {}, 만료 시간 = {}", jwtToken, expiresAt);

        tokenBlacklistService.addToBlacklist(jwtToken, expiresAt);
        logger.info("토큰을 블랙리스트에 추가: {}", jwtToken);

        return ResponseEntity.ok("{\"message\": \"Successfully logged out\"}");
    }

    @PostMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }

        if (!jwtTokenProvider.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return ResponseEntity.ok("Access granted");
    }

    public LocalDateTime extractTokenExpiration(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
        } catch (SignatureException e) {
            // 서명 오류 처리
            throw new RuntimeException("Invalid JWT signature");
        } catch (Exception e) {
            // 기타 오류 처리
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
