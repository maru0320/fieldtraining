package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Professor;

public interface ProfessorRepository  extends JpaRepository<Professor, Long>{
	
	Professor getByUserId(Long professorId);

}
