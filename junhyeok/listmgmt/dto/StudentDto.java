package com.fieldtraining.listmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  // 기본 생성자 생성
public class StudentDto {
	private Long id;
    private String name;
    private String college;
    private String department;
    private String email;
    private String subject;
    private String phoneNumber;
}
