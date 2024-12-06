package com.fieldtraining.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User getByUserId(String userId);
	
	Optional<User> findByUserId(String userId);
	
	Optional<User> findById(Long id);

	

	boolean existsByUserId(String userId);
}
