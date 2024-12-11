package com.fieldtraining.managerInfo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeManagerDto {
	private Long id;
	private String address;
	private String officeNumber;
	private String college;
}
