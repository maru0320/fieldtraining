package com.packt.fieldtraining.ManagerInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.ManagerInfo.dto.ManagerInfoDto;
import com.packt.fieldtraining.ManagerInfo.dto.ManagerInfoUpdateDto;
import com.packt.fieldtraining.common.entity.Manager;
import com.packt.fieldtraining.common.entity.User;
import com.packt.fieldtraining.common.repository.ManagerRepository;
import com.packt.fieldtraining.common.repository.UserRepository;

@Service
public class ManagerInfoService {
	
	@Autowired
	private final ManagerRepository managerRepository;
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public ManagerInfoService(ManagerRepository managerRepository, UserRepository userRepository) {
		this.managerRepository = managerRepository;
		this.userRepository = userRepository;
	}
	
	public ManagerInfoDto getManagerInfo(Long managerId) {
		// Manager 데이터베이스 조회
	    Manager manager = managerRepository.findById(managerId)
	            .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + managerId));

	    // User 정보 추출
	    User user = manager.getUser();

        // 비밀번호 처리 (앞 3자리는 표시, 나머지는 마스킹)
        String password = user.getPassword();
        String passwordPrefix = password.substring(0, 3);
        String passwordMasked = passwordPrefix + "*".repeat(password.length() - 3);

        // DTO 생성 및 반환
        return ManagerInfoDto.builder()
                .institutionName(manager.getInstitutionName())
                .userId(user.getUserId())
                .passwordPrefix(passwordPrefix)
                .passwordMasked(passwordMasked)
                .officePhoneNumber(manager.getOfficeNumber())
                .address(manager.getAddress())
                .build();
    }
	
	//리액트에서 정보를 받아서 DB로 수정하는 기능
}
