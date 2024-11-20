package com.packt.fieldtraining.service;

import com.packt.fieldtraining.data.dto.SignInResultDto;
import com.packt.fieldtraining.data.dto.SignUpResultDto;

public interface SignService {

	SignUpResultDto join(String id,
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
	
	
	SignInResultDto login(String id, String password) throws RuntimeException;


}
