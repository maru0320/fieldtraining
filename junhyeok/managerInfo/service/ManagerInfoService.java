package com.fieldtraining.managerInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fieldtraining.managerInfo.dto.CollegeManagerDto;
import com.fieldtraining.managerInfo.dto.CollegeUpdateDto;
import com.fieldtraining.managerInfo.dto.SchoolManagerDto;
import com.fieldtraining.managerInfo.dto.SchoolUpdateDto;
import com.fieldtraining.managerInfo.repository.CollegeRepository;
import com.fieldtraining.managerInfo.repository.SchoolRepository;
import com.fieldtraining.managerInfo.repository.UserRepository;

@Service
public class ManagerInfoService {
    private CollegeRepository collegeRepository;
    private SchoolRepository schoolRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Autowired
    public ManagerInfoService(CollegeRepository collegeRepository, 
    		SchoolRepository schoolRepository, BCryptPasswordEncoder passwordEncoder,
    		UserRepository userRepository) {
        this.collegeRepository = collegeRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    
    public Object getManagerInfo(String userId, String role) {
        if ("school_manager".equals(role)) {
            return findSchoolManagerInfo(userId);
        } else if ("college_manager".equals(role)) {
            return findCollegeManagerInfo(userId);
        }
        return null;
    }

    private SchoolManagerDto findSchoolManagerInfo(String userId) {
        return schoolRepository.findByUser_UserId(userId)
                .map(manager -> SchoolManagerDto.builder()
                        .address(manager.getAddress())
                        .officeNumber(manager.getOfficeNumber())
                        .schoolName(manager.getSchoolName())
                        .build())
                .orElse(null);
    }

    private CollegeManagerDto findCollegeManagerInfo(String userId) {
        return collegeRepository.findByUser_UserId(userId)
                .map(manager -> CollegeManagerDto.builder()
                        .address(manager.getAddress())
                        .officeNumber(manager.getOfficeNumber())
                        .college(manager.getCollege())
                        .build())
                .orElse(null);
    }
    
    // 학교 관리자 정보 업데이트
    public void updateSchoolManagerInfo(String userId, SchoolUpdateDto managerInfoDto) {
        schoolRepository.findByUser_UserId(userId)
                .ifPresent(manager -> {
                    manager.setAddress(managerInfoDto.getAddress()); // 주소 업데이트
                    manager.setOfficeNumber(managerInfoDto.getOfficePhoneNumber()); // 사무실 번호 업데이트

                    // 비밀번호 업데이트
                    if (managerInfoDto.getPassword() != null && !managerInfoDto.getPassword().isEmpty()) {
                        userRepository.findByUserId(userId).ifPresent(user -> {
                            user.setPassword(passwordEncoder.encode(managerInfoDto.getPassword())); // 비밀번호 업데이트
                            userRepository.save(user); // User 엔티티 저장
                        });
                    }

                    manager.setSchoolName(managerInfoDto.getSchoolName()); // 학교 이름 업데이트
                    schoolRepository.save(manager); // 변경사항 저장
                });
    }

    // 대학 관리자 정보 업데이트
    public void updateCollegeManagerInfo(String userId, CollegeUpdateDto managerInfoDto) {
        collegeRepository.findByUser_UserId(userId)
                .ifPresent(manager -> {
                    manager.setAddress(managerInfoDto.getAddress()); // 주소 업데이트
                    manager.setOfficeNumber(managerInfoDto.getOfficePhoneNumber()); // 사무실 번호 업데이트

                    // 비밀번호 업데이트
                    if (managerInfoDto.getPassword() != null && !managerInfoDto.getPassword().isEmpty()) {
                        userRepository.findByUserId(userId).ifPresent(user -> {
                            user.setPassword(passwordEncoder.encode(managerInfoDto.getPassword())); // 비밀번호 업데이트
                            userRepository.save(user); // User 엔티티 저장
                        });
                    }

                    manager.setCollege(managerInfoDto.getCollege()); // 대학 이름 업데이트
                    collegeRepository.save(manager); // 변경사항 저장
                });
    }
}
