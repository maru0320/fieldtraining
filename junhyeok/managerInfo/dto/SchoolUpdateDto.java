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
public class SchoolUpdateDto {
	private Long id;
	private String address; // 주소
    private String officeNumber; // 사무실 전화번호
    private String password; // 변경할 비밀번호
    private String schoolName; // 학교 이름
}
