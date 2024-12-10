package com.fieldtraining.data.entity;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fieldtraining.data.repository.UserRepository;

@Component
public class AdminAccountInitializer implements CommandLineRunner{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception{
		if (!userRepository.existsByUserId("admin")) {
			User admin = User.builder()
					.userId("admin")
					.password(passwordEncoder.encode("1234"))
					.role("admin")
					.roles(Collections.singletonList("ADMIN"))
					.isApproval(true)
					.build();
			userRepository.save(admin);
			System.out.println("기본 관리자 계정 생성");
		}
	}

}
