package com.packt.fieldtraining.matching.dto;

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
}
