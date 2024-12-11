package com.fieldtraining.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.attendance.dto.AttendanceRequestDto;
import com.fieldtraining.attendance.dto.AttendanceSummaryDto;
import com.fieldtraining.attendance.service.AttendanceService;

//AttendanceController.java

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	private final AttendanceService attendanceService;

	@Autowired
	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

    // 출석 요청 API
    @PostMapping("/request")
    public ResponseEntity<String> requestAttendance(@RequestBody AttendanceRequestDto attendanceRequestDto) {
        try {
            attendanceService.requestAttendance(attendanceRequestDto);  // 출석 요청 처리
            return ResponseEntity.ok("출석 요청이 성공적으로 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // 이미 출석 요청한 경우
        } catch (Exception e) {
            return ResponseEntity.status(500).body("출석 요청 처리 중 오류가 발생했습니다.");
        }
    }

    // 출석 요청 상태 확인 API (학생 ID를 기준으로 출석 요청 여부 확인)
    @GetMapping("/check/{studentId}")
    public ResponseEntity<Boolean> checkAttendanceRequest(@PathVariable Long studentId) {
        boolean hasRequested = attendanceService.hasRequestedAttendance(studentId);  // 출석 요청 여부 확인
        return ResponseEntity.ok(hasRequested);
    }

	// 선생님이 출석 요청 목록 조회
	@GetMapping("/requests/{teacherId}")
	public List<AttendanceRequestDto> getAttendanceRequests(@PathVariable Long teacherId) {
		return attendanceService.getAttendanceRequestsForTeacher(teacherId);
	}

	//학생 출석 목록 조회
	@GetMapping("/status/{studentId}")
	public List<AttendanceRequestDto> getAttendanceStatus(@PathVariable Long studentId) {
		return attendanceService.getAttendanceListForStudent(studentId);
	}

	// 출석 상태 업데이트 (선생님)
	@PutMapping("/update/{attendanceId}")
	public void updateAttendanceStatus(
			@PathVariable Long attendanceId,
			@RequestParam("attendanceStatus") String attendanceStatus) {

		attendanceService.updateAttendanceStatus(attendanceId, attendanceStatus);
	}

	// 선생님이 자신의 매칭된 학생 출석 목록 조회
    @GetMapping("/status/teacher/{teacherId}")
    public List<AttendanceSummaryDto> getAttendanceRecordsForTeacher(@PathVariable("teacherId") Long teacherId) {
        return attendanceService.getAttendanceRecordsForTeacher(teacherId);
    }


}
