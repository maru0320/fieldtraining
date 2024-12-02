package com.packt.fieldtraining.service;

import org.springframework.web.multipart.MultipartFile;

import com.packt.fieldtraining.data.dto.SignInResultDto;
import com.packt.fieldtraining.data.dto.SignUpResultDto;

public interface SignService {

	SignUpResultDto generalJoin(
			String userId,
			String name,
			String password, 
			String role,
			String college,
			String department,
			int studentNumber,
			String subject,
			String email,
			String phoneNumber,
			String schoolName,
			String officeNumber,
			String address,
			boolean isApproval);
	
	SignUpResultDto institutionalJoin(
			String userId,
			String password, 
			String officeNumber,
			boolean isApproval,
			String address,
			MultipartFile proofData,
			String managerId,
			String role);
	
	
	SignInResultDto login(String id, String password) throws RuntimeException;


}
