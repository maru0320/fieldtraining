package com.fieldtraining.managerInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fieldtraining.data.entity.CollegeManager;

@Repository
public interface CollegeRepository extends JpaRepository<CollegeManager, Long>{

	
}
