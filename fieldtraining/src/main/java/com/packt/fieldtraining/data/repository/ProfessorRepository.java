package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.packt.fieldtraining.data.entity.Professor;

@Repository
public interface ProfessorRepository  extends JpaRepository<Professor, Long>{

}
