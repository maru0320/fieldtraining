package com.packt.fieldtraining.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.SchoolManager;

public interface SchoolManagerRepository extends JpaRepository<SchoolManager, Long>{
	Optional<SchoolManager> findById(Long id);
}
