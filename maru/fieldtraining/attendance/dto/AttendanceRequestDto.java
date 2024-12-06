package com.fieldtraining.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {

    private Long attendanceId;  // 출석 요청 ID
    private Long studentId;     // 학생 ID
    private String studentName; // 학생 이름
    private String date;        // 출석 날짜
    private String status;      // 출석 상태 ("ATTENDED", "LATE", "EARLY_LEAVE", "ABSENT")


}
