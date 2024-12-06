package com.fieldtraining.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.Student;

public interface StudentRepository  extends JpaRepository<Student, Long>{
	
	Student getByUserId(Long studentId);

	List<Student> findAll();  // 전체 학생 목록을 반환
	
	Optional<Student> findByNameAndEmail(String name, String email);
}
