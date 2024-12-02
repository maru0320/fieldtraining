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
public class ManagerInfoDto {
	private String institutionName; // 기관 이름
    private String userId; // 아이디
    private String passwordPrefix; // 비밀번호 앞 3자리
    private String passwordMasked; // 마스킹된 비밀번호
    private String officePhoneNumber; // 사무실 전화번호
    private String address; // 주소 
}
