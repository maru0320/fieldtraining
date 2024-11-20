package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Manager;

public interface ManagerRepository  extends JpaRepository<Manager, Long>{
	
	Manager getByUserId(Long managerId);

}
