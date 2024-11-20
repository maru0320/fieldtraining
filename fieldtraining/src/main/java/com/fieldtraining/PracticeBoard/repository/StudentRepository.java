package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
