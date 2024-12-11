package com.fieldtraining.matchingCheck.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MatchingDto {
	private String studentName;
    private String teacherName;
    private String matchStatus;
    private boolean matchApproved;
}
