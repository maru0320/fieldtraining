package com.fieldtraining.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.data.entity.User;


public interface TeacherRepository extends JpaRepository<Teacher, Long>{

	Teacher getByUserId(Long teacherId);

	List<Teacher> findByUser(User user);

	List<Teacher> findAll();  // 전체 선생님 목록을 반환

	// suject로 선생님을 찾는 메서드
	List<Teacher> findBySubject(String subject);
}