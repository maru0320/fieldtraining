package com.fieldtraining.attendance.dto;


public class AttendanceStatusDto {

    private String status; // "대기", "출석", "지각", "조퇴", "결석"

    // getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}