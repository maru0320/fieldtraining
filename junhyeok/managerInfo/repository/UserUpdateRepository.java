package com.fieldtraining.managerInfo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.User;

public interface UserUpdateRepository extends JpaRepository<User, Long>{
	// 사용자 ID로 사용자 조회
    Optional<User> findByUserId(String userId);
}
