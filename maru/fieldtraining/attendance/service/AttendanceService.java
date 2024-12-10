package com.fieldtraining.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.attendance.dto.AttendanceRequestDto;
import com.fieldtraining.attendance.dto.AttendanceSummaryDto;
import com.fieldtraining.attendance.entity.Attendance;
import com.fieldtraining.attendance.entity.AttendanceStatus;
import com.fieldtraining.attendance.repository.AttendanceRepository;
import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.repository.MatchRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, MatchRepository matchRepository) {
        this.attendanceRepository = attendanceRepository;
        this.matchRepository = matchRepository;
    }

    // 출석 목록 조회 (학생)
    public List<AttendanceRequestDto> getAttendanceListForStudent(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);

        // 엔티티 -> DTO 변환
        return attendances.stream()
                .map(attendance -> new AttendanceRequestDto(
                        attendance.getAttendanceId(),
                        attendance.getStudent().getId(),
                        attendance.getStudent().getName(),
                        attendance.getDate(),
                        attendance.getStatus().name()))  // AttendanceStatus를 문자열로 변환
                .collect(Collectors.toList());
    }

    // 출석 요청 처리 (학생)
    public void requestAttendance(AttendanceRequestDto attendanceRequestDto) {
        // 매칭된 선생님 찾기
        Match match = matchRepository.findByStudentId(attendanceRequestDto.getStudentId());

        // 출석 요청 생성
        Attendance attendance = new Attendance();
        attendance.setStudent(match.getStudent());  // 학생 설정
        attendance.setTeacher(match.getTeacher());  // 선생님 설정
        attendance.setDate(LocalDate.now().toString());  // 출석 요청 날짜는 오늘 날짜로 설정
        attendance.setStatus(AttendanceStatus.PENDING);  // 출석 상태는 대기(PENDING)로 설정

        attendanceRepository.save(attendance);  // 출석 정보 저장
    }

    // 출석 상태 업데이트 (선생님)
    public void updateAttendanceStatus(Long attendanceId, String status) {
        // 출석 요청 찾기
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("출석 정보가 없습니다."));

        // status 값을 AttendanceStatus로 변환
        AttendanceStatus attendanceStatus = AttendanceStatus.valueOf(status);

        // 출석 상태 업데이트
        attendance.setStatus(attendanceStatus);
        attendanceRepository.save(attendance);  // 상태 저장
    }
    
    // 선생님 ID를 기반으로 출석 요청 목록 조회
    public List<AttendanceRequestDto> getAttendanceRequestsForTeacher(Long teacherId) {
        List<Attendance> attendances = attendanceRepository.findByTeacherId(teacherId);

        // 엔티티 -> DTO 변환
        return attendances.stream()
                .map(attendance -> new AttendanceRequestDto(
                        attendance.getAttendanceId(),
                        attendance.getStudent().getId(),
                        attendance.getStudent().getName(),
                        attendance.getDate(),
                        attendance.getStatus().name()))  // AttendanceStatus를 문자열로 변환
                .collect(Collectors.toList());
    }
    
    public List<AttendanceSummaryDto> getAttendanceRecordsForTeacher(Long teacherId) {
    	List<Attendance> attendances = attendanceRepository.findByTeacherId(teacherId);
    	
    	return attendances.stream()
    			.map(attendance -> new AttendanceSummaryDto(
    					attendance.getAttendanceId(),
    					attendance.getDate(),
    					attendance.getStatus(),
    					attendance.getStudent().getName(),
    					attendance.getTeacher().getName()
    			))
    			.collect(Collectors.toList());
    }
    

}
