package com.fieldtraining.managerInfo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fieldtraining.data.entity.CollegeManager;

@Repository
public interface CollegeRepository extends JpaRepository<CollegeManager, Long>{

	Optional<CollegeManager> findByUser_UserId(String userId);
	
}
