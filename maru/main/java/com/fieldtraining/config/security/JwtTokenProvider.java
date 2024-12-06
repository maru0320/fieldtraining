package com.fieldtraining.config.security;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.fieldtraining.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
		claims.put("userId", id);
		claims.put("roles", roles.stream()
				.map(role -> "ROLE_" + role)  // ROLE_ 접두어 추가
				.collect(Collectors.toList()));

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
        // 토큰에서 사용자 정보 추출
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();

        String username = claims.getSubject();  // 사용자 이름 (혹은 사용자 ID)

        // 해당 사용자의 권한을 설정
        List<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                                                   .map(SimpleGrantedAuthority::new)
                                                   .collect(Collectors.toList());

        User principal = new User(username, "", authorities);  // 사용자 객체 생성
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
		LOGGER.info("[resolveToken] 토큰 값: {}", bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 이후의 값 반환
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			// 블랙리스트 체크
			if (tokenBlacklistService.isTokenBlacklisted(token)) {
				LOGGER.info("[validateToken] 토큰 유효성검사 실패 (블랙리스트 등록) {}", token);
				return false; // 블랙리스트에 등록된 토큰은 무효화
			}

			// 토큰 파싱 및 서명 검증
			Jws<Claims> claims = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token);

			// 만료 시간 체크
			Date expiration = claims.getBody().getExpiration();
			if (expiration == null) {
				LOGGER.error("[validateToken] 토큰에 만료 시간(exp) 클레임이 없습니다.");
				return false;
			}

			if (expiration.before(new Date())) {
				LOGGER.info("[validateToken] 토큰이 만료되었습니다. Expiration: {}", expiration);
				return false;
			}

			return true;

		} catch (JwtException | IllegalArgumentException e) {
			LOGGER.error("[validateToken] JWT 파싱 실패, 오류: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			LOGGER.error("[validateToken] 예외 발생: {}", e.getMessage(), e);
			return false;
		}
	}
}
