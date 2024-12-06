package com.fieldtraining.attendance.dto;

import com.fieldtraining.attendance.entity.AttendanceStatus;
import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto{
    private Long attendanceId;
    private String date;
    private AttendanceStatus status;
    private Student student;   // 학생 정보
    private Teacher teacher;   // 매칭된 선생님 정보
}