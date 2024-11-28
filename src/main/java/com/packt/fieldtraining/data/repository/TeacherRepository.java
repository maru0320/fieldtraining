package com.packt.fieldtraining.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.data.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	 List<Teacher> findAll();  // 전체 선생님 목록을 반환
	 
	 // suject로 선생님을 찾는 메서드
	 List<Teacher> findBySubject(String subject);
}
