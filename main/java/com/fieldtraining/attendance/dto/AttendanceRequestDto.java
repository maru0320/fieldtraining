package com.fieldtraining.attendance.dto;

import com.fieldtraining.attendance.entity.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {
	private Long Id;
    private Long studentId;    // 학생 ID
    private String studentName; // 학생 이름
    private String date;       // 출석 요청 날짜
    private AttendanceStatus status; // 출석 상태
}
