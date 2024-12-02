package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.CollegeManager;

public interface CollegeManagerRepository  extends JpaRepository<CollegeManager, Long>{
	
	

}
