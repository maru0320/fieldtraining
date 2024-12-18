package com.fieldtraining.managerInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.data.entity.CollegeManager;
import com.fieldtraining.data.entity.SchoolManager;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.managerInfo.dto.CollegeUpdateDto;
import com.fieldtraining.managerInfo.dto.CurrentManagerDto;
import com.fieldtraining.managerInfo.dto.SchoolUpdateDto;
import com.fieldtraining.managerInfo.repository.CollegeRepository;
import com.fieldtraining.managerInfo.repository.SchoolRepository;
import com.fieldtraining.managerInfo.repository.UserUpdateRepository;

@Service
public class ManagerInfoService {
    private CollegeRepository collegeRepository;
    private SchoolRepository schoolRepository;
    private UserUpdateRepository userRepository;
    
    @Autowired
    public ManagerInfoService(CollegeRepository collegeRepository, 
    		SchoolRepository schoolRepository,
    		UserUpdateRepository userRepository) {
        this.collegeRepository = collegeRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }
    // 매니저 페이지에 보여줄 정보
    public Object getManagerInfoById(Long id, String role) {
        if ("collegeManager".equals(role)) {
            return collegeRepository.findById(id);
        } else if ("schoolManager".equals(role)) {
            return schoolRepository.findById(id);
        }
        return null;
    }
    
    // 수정 폼에 보여줄 정보
    public CurrentManagerDto getManagerInfoForEdit(Long id, String role) {
        if ("collegeManager".equals(role)) {
            return collegeRepository.findById(id)
                    .map(manager -> new CurrentManagerDto(
                            manager.getId(),
                            manager.getAddress(),
                            manager.getOfficeNumber(),
                            manager.getCollege(),
                            null // 학교 이름은 null
                    ))
                    .orElse(null);
        } else if ("schoolManager".equals(role)) {
            return schoolRepository.findById(id)
                    .map(manager -> new CurrentManagerDto(
                            manager.getId(),
                            manager.getAddress(),
                            manager.getOfficeNumber(),
                            null, // 대학 이름은 null
                            manager.getSchoolName()
                    ))
                    .orElse(null);
        }
        return null;
    }
    
    // 학교 관리자 정보 업데이트
    public void updateSchoolManagerInfo(Long id, SchoolUpdateDto managerInfoDto) {
        schoolRepository.findById(id)
                .ifPresent(manager -> {
                    manager.setAddress(managerInfoDto.getAddress()); // 주소 업데이트
                    manager.setOfficeNumber(managerInfoDto.getOfficeNumber()); // 사무실 번호 업데이트
                    // 비밀번호 업데이트 (평문으로 저장)
                    if (managerInfoDto.getPassword() != null && !managerInfoDto.getPassword().isEmpty()) {
                        userRepository.findById(id).ifPresent(user -> {
                            user.setPassword(managerInfoDto.getPassword()); // 비밀번호 업데이트
                            userRepository.save(user); // User 엔티티 저장
                        });
                    }
                    manager.setSchoolName(managerInfoDto.getSchoolName()); // 학교 이름 업데이트
                    schoolRepository.save(manager); // 변경사항 저장
                });
    }

    // 대학 관리자 정보 업데이트
    public void updateCollegeManagerInfo(Long id, CollegeUpdateDto managerInfoDto) {
        collegeRepository.findById(id)
                .ifPresent(manager -> {
                    manager.setAddress(managerInfoDto.getAddress()); // 주소 업데이트
                    manager.setOfficeNumber(managerInfoDto.getOfficeNumber()); // 사무실 번호 업데이트
                    // 비밀번호 업데이트 (평문으로 저장)
                    if (managerInfoDto.getPassword() != null && !managerInfoDto.getPassword().isEmpty()) {
                        userRepository.findById(id).ifPresent(user -> {
                            user.setPassword(managerInfoDto.getPassword()); // 비밀번호 업데이트
                            userRepository.save(user); // User 엔티티 저장
                        });
                    }
                    manager.setCollege(managerInfoDto.getCollege()); // 대학 이름 업데이트
                    collegeRepository.save(manager); // 변경사항 저장
                });
    }
}