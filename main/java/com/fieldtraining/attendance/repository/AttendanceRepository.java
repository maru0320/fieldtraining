package com.fieldtraining.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.attendance.entity.Attendance;
import com.fieldtraining.attendance.entity.AttendanceStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentId(Long studentId);
    
    // 선생님 ID와 출석 상태를 기준으로 출석 요청을 찾는 메서드
    List<Attendance> findByTeacherIdAndStatus(Long teacherId, AttendanceStatus status);
    
    List<Attendance> findByTeacherId(Long teacherId);
}
