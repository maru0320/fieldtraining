package com.packt.fieldtraining.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.packt.fieldtraining.common.entity.Professor;

@Repository
public interface ProfessorRepository  extends JpaRepository<Professor, Long>{

}
