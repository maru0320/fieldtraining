package com.fieldtraining.matching.dto;

import com.fieldtraining.matching.entity.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestViewDto {

    private Long matchId;
    private String studentName;
    private String studentNumber;
    private boolean matchApproved;

    public MatchRequestViewDto(Long matchId, String studentName, String studentNumber, MatchStatus status) {
        this.matchId = matchId;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.matchApproved = status == MatchStatus.APPROVED;
    }
}
