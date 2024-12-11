package com.fieldtraining.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.attendance.entity.Attendance;
import com.fieldtraining.attendance.entity.AttendanceStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // 선생님 ID와 출석 상태를 기준으로 출석 요청을 찾는 메서드
    List<Attendance> findByTeacherIdAndStatus(Long teacherId, AttendanceStatus status);
    
    // 학생 ID를 기반으로 출석 기록을 조회하는 메서드
    List<Attendance> findByStudentId(Long studentId);

    // 선생님 ID를 기반으로 출석 기록을 조회하는 메서드
    List<Attendance> findByTeacherId(Long teacherId);

    // 학생 ID와 날짜를 기준으로 출석 기록을 찾는 메서드
    Attendance findByStudentIdAndDate(Long studentId, String date);

    // 학생 ID와 날짜를 기준으로 출석 요청이 이미 있는지 확인하는 메서드
    boolean existsByStudentIdAndDate(Long studentId, String date);
}