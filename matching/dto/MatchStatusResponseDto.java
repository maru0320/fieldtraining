package com.packt.fieldtraining.matching.dto;

import com.packt.fieldtraining.matching.entity.MatchStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchStatusResponseDto {

    private MatchStatus matchStatus; // 매칭 상태

    public MatchStatusResponseDto() {}

    public MatchStatusResponseDto(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }
}
