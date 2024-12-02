package com.packt.fieldtraining.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.data.entity.TokenBlacklist;
import com.packt.fieldtraining.data.repository.TokenBlacklistRepository;

@Service
public class TokenBlacklistService {

	@Autowired
	private TokenBlacklistRepository tokenBlacklistRepository;

	// 블랙리스트에 토큰 추가 
	 public void addToBlacklist(String token, LocalDateTime expiresAt) {
	        TokenBlacklist tokenBlacklist = new TokenBlacklist();
	        tokenBlacklist.setToken(token);
	        tokenBlacklist.setExpiresAt(expiresAt);
	        tokenBlacklist.setCreatedAt(LocalDateTime.now());
	        tokenBlacklistRepository.save(tokenBlacklist);
	    }


	// 블랙리스트에서 토큰 확인
	public boolean isTokenBlacklisted(String token) {
//		Optional<TokenBlacklist> tokenRecord = tokenBlacklistRepository.findByToken(token);
//		return tokenRecord.isPresent();
		return false;
	}

	// 블랙리스트에서 토큰 제거 (토큰이 만료되었을 때)
	@Scheduled(fixedRate = 3600000)
	public void removeExpiredTokens() {
		tokenBlacklistRepository.deleteByExpiresAtBefore(LocalDateTime.now());
	}
}
