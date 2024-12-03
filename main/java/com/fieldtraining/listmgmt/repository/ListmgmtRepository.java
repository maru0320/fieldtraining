package com.fieldtraining.listmgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fieldtraining.data.entity.User;

public interface ListmgmtRepository extends JpaRepository<User, Long>{

	 // Teacher와 연관된 User만 가져오기
    @Query("SELECT u FROM User u WHERE u.teacherDetail IS NOT NULL")
    List<User> findTeachers();

    // Student와 연관된 User만 가져오기
    @Query("SELECT u FROM User u WHERE u.studentDetail IS NOT NULL")
    List<User> findStudents();
}
