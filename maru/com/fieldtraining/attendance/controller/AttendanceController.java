package com.fieldtraining.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	// 출석 요청 처리 (학생)
	@PostMapping("/request")
	public void requestAttendance(@RequestBody AttendanceRequestDto attendanceRequestDto) {
		attendanceService.requestAttendance(attendanceRequestDto);
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
