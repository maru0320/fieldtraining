package com.packt.fieldtraining.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDto {

	private Long matchId;
	private String studentName;
	private String teacherName;
	private boolean matchApproved;
}
