package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User getByUserId(String userId);

	boolean existsByUserId(String userId);
}
