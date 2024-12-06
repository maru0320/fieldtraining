package com.fieldtraining.mypage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.mypage.dto.MypageStudentDto;

@Service
public class MypageService {
    
    @Autowired
    UserRepository userRepository;
    
    //마이페이지 정보 보내기
    public MypageStudentDto getUserInfo (Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if ("student".equals(user.getRole())) {
            return MypageStudentDto.builder()
                    .name(user.getStudentDetail().getName())
                    .role(user.getRole())
                    .college(user.getStudentDetail().getCollege())
                    .department(user.getStudentDetail().getDepartment())
                    .email(user.getStudentDetail().getEmail())
                    .phoneNumber(user.getStudentDetail().getPhoneNumber())
                    .schoolName(user.getStudentDetail().getSchoolName())
                    .studentNumber(user.getStudentDetail().getStudentNumber())
                    .subject(user.getStudentDetail().getSubject())
                    .build();
//        } else if ("teacher".equals(user.getRole())) {
//            return MypageTeacherDto.builder()
//                    .name(user.getTeacherDetail().getName())
//                    .role(user.getRole())
//                    .email(user.getTeacherDetail().getEmail())
//                    .phoneNumber(user.getTeacherDetail().getPhoneNumber())
//                    .schoolName(user.getTeacherDetail().getSchoolName())
//                    .subject(user.getTeacherDetail().getSubject())
//                    .officeNumber(user.getTeacherDetail().getOfficeNumber())
//                    .build();
//        } else if ("professor".equals(user.getRole())) {
//            return MypageProfessorDto.builder()
//                    .name(user.getProfessorDetail().getName())
//                    .role(user.getRole())
//                    .college(user.getProfessorDetail().getCollege())
//                    .department(user.getProfessorDetail().getDepartment())
//                    .email(user.getProfessorDetail().getEmail())
//                    .phoneNumber(user.getProfessorDetail().getPhoneNumber())
//                    .officeNumber(user.getProfessorDetail().getOfficeNumber())
//                    .build();
        } else {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }
    }
}
