package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
