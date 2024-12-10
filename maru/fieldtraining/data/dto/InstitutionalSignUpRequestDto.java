package com.fieldtraining.data.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionalSignUpRequestDto {

	private String id;
	private String userId;
	private String password;
	private String role;
	private String officeNumber;
	private String address;
	private boolean isApproval;
	private MultipartFile  proofData;
	private String managerId;
}
