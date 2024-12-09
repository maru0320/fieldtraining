package com.fieldtraining.data.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fieldtraining.data.entity.TokenBlacklist;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

	// 토큰 존재 여부 확인
    Optional<TokenBlacklist> findByToken(String token);

    // 만료된 토큰 삭제 (필요 시)
    void deleteByExpiresAtBefore(LocalDateTime now);
}