package com.fieldtraining.matching.dto;

public class MatchStatusDto {
    private String status;

    public MatchStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
