package com.packt.fieldtraining.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.data.entity.User;


public interface TeacherRepository extends JpaRepository<Teacher, Long>{
	
	Teacher getByUserId(Long teacherId);

	List<Teacher> findByUser(User user);
}
