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
public class SignUpRequestDto {

	private String id;
	private String userId;
	private String name;
	private String password;
	private String role;
	private String college;
	private String department;
	private int studentNumber;
	private String subject;
	private String email;
	private String phoneNumber;
	private String schoolName;
	private String officeNumber;
	private String address;
	private boolean isApproval;
}
