package com.packt.fieldtraining.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestViewDto {

	private Long matchId;
	private String studentName;
	private String studentNumber;
	private boolean matchApproved;
}
