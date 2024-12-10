package com.fieldtraining.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDto {

    private Long studentId;  // 학생 ID
    private Long teacherId;  // 선생님 ID
    private boolean approve; // 승인 여부

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }
}
