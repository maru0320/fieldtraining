package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Student;

public interface StudentRepository  extends JpaRepository<Student, Long>{
	
	Student getByUserId(Long studentId);

}
