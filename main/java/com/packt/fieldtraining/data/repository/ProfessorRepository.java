package com.packt.fieldtraining.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Professor;
import com.packt.fieldtraining.data.entity.User;

public interface ProfessorRepository  extends JpaRepository<Professor, Long>{
	
	List<Professor> findByUser(User user);
	
	Optional<Professor> findById(Long id);

}
