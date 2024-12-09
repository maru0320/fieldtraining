package com.fieldtraining.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User getByUserId(String userId);
	
	Optional<User> findByUserId(String userId);
	
	Optional<User> findById(Long id);
	
	 List<User> findByRole(String role);  // 특정 역할만 조회
	 
	 List<User> findByIsApproval(boolean isApproval);  // 승인 여부로 조회
	 
	boolean existsByIdAndIsApproval(Long id, boolean isApproval);
	 
	boolean existsByUserId(String userId);
}
