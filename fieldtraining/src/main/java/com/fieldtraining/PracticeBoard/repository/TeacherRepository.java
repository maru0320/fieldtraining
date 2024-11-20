package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
