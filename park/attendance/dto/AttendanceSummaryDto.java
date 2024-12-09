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
public class AttendanceSummaryDto {
    private Long attendanceId;
    private String date;
    private AttendanceStatus status;
    private String studentName;   // 학생 이름
    private String teacherName;   // 선생님 이름
}