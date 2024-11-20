package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Teacher;


public interface TeacherRepository extends JpaRepository<Teacher, Long>{
	
	Teacher getByUserId(Long teacherId);

}
