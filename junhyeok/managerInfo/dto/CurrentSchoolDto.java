package com.fieldtraining.managerInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentSchoolDto {
	private Long id;
	private String address;
    private String officeNumber;
    private String schoolName;
    private String userId;
    private String password;
}