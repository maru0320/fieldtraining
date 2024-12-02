package com.fieldtraining.config.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.fieldtraining.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
	private final UserDetailsService userDetailsService;
	
	@Value("${springboot.jwt.secret}")
	private String secretKey;
	
	private final long tokenValidMillisecond = 1000L * 60 * 60;
	private final TokenBlacklistService tokenBlacklistService;
	
	//SecretKey에 대해 인코딩 수행
	@PostConstruct
	protected void init() {
		if (tokenBlacklistService == null) {
			System.out.println("TokenBlacklistService가 null입니다!");
		}
		LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
		LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
	}
	
	//JWT 토큰 생성
	public String createToken(Long id, String userId ,List<String> roles) {
		LOGGER.info("[createToken] 토큰 생성 시작");
		Claims claims = Jwts.claims().setSubject(String.valueOf(id));
		claims.put("userId", userId);
		claims.put("roles", roles);

		Date now = new Date();
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenValidMillisecond))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();

		LOGGER.info("[createToken] 토큰 생성 완료");
		return token;
	}
	// JWT 토큰으로 인증 정보 조회
	public Authentication getAuthentication(String token) {
		LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
		LOGGER.info("[getAuthentication] 토큰 인정 정보 조회 완료, UserDetail UserName : {}",
				userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, "",
				userDetails.getAuthorities());
	}
	
	//JWT 토큰에서 회원 구별 정보 추출
	public String getUsername(String token) {
		LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
		String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
				.getSubject();
		LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
		return info;
	}
	
	// HTTP Request Header 에 설정된 토큰 값을 가져옴
	public String resolveToken(HttpServletRequest request) {
	    LOGGER.info("[resolveToken] HTTP 헤더에서 Authorization 값 추출");
	    String bearerToken = request.getHeader("Authorization");
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7); // "Bearer " 이후의 값 반환
	    }
	    return null;
	}
	
	public boolean validateToken(String token) {
	    try {
	        if (tokenBlacklistService.isTokenBlacklisted(token)) {
	            return false; // 블랙리스트에 등록된 토큰은 무효화
	        }

	        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	        return !claims.getBody().getExpiration().before(new Date());
	    } catch (Exception e) {
	        return false;
	    }
	}
}
