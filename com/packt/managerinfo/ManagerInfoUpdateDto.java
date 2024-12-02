package com.packt.fieldtraining.ManagerInfo.dto;

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
public class ManagerInfoUpdateDto {
	private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String officeNumber;
    private String institutionName;
    private String password; // 비밀번호 (수정 시에만 포함)
}
