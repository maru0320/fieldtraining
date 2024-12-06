package com.fieldtraining.attendance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AttendanceStatus {
    ATTENDED,       // 출석
    LATE,           // 지각
    EARLY_LEAVE,    // 조퇴
    ABSENT,         // 결석
    PENDING         // 대기
}