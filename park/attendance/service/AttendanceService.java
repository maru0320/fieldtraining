package com.fieldtraining.attendance.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.attendance.dto.AttendanceRequestDto;
import com.fieldtraining.attendance.dto.AttendanceSummaryDto;
import com.fieldtraining.attendance.entity.Attendance;
import com.fieldtraining.attendance.entity.AttendanceStatus;
import com.fieldtraining.attendance.repository.AttendanceRepository;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.repository.MatchRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MatchRepository matchRepository;
    private final StudentRepository studentRepository; // Student 엔티티를 조회하기 위한 Repository
    private final TeacherRepository teacherRepository; // Teacher 엔티티를 조회하기 위한 Repository



    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, MatchRepository matchRepository,
			StudentRepository studentRepository, TeacherRepository teacherRepository) {
		super();
		this.attendanceRepository = attendanceRepository;
		this.matchRepository = matchRepository;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
	}
    
    // 출석 요청 여부 확인
    public boolean hasRequestedAttendance(Long studentId) {
        // 오늘 날짜를 "yyyy-MM-dd" 형식으로 변환
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        // 학생 ID와 날짜로 출석 기록을 확인
        Attendance attendance = attendanceRepository.findByStudentIdAndDate(studentId, today);
        return attendance != null;
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
    
    

    // 출석 요청 처리
    public void requestAttendance(AttendanceRequestDto attendanceRequestDto) {
        // 오늘 날짜를 "yyyy-MM-dd" 형식으로 변환
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 이미 출석 요청을 했는지 확인
        boolean alreadyRequestedToday = attendanceRepository.existsByStudentIdAndDate(attendanceRequestDto.getStudentId(), today);
        if (alreadyRequestedToday) {
            throw new IllegalArgumentException("오늘은 이미 출석 요청을 하였습니다.");
        }

        // 매칭된 선생님 찾기
        Match match = matchRepository.findByStudentId(attendanceRequestDto.getStudentId());

        // 출석 요청 생성
        Attendance attendance = new Attendance();
        attendance.setStudent(match.getStudent());
        attendance.setTeacher(match.getTeacher());
        attendance.setDate(today);  // 날짜는 오늘 날짜로 설정
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
