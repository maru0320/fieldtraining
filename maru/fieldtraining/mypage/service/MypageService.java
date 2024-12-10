package com.fieldtraining.mypage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.mypage.dto.MypageProfessorDto;
import com.fieldtraining.mypage.dto.MypageResponseDto;
import com.fieldtraining.mypage.dto.MypageStudentDto;
import com.fieldtraining.mypage.dto.MypageTeacherDto;

@Service
public class MypageService {
    
    @Autowired
    UserRepository userRepository;
    
    //마이페이지 정보 보내기
    public MypageResponseDto userInfo (Long id) {
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
        } else if ("teacher".equals(user.getRole())) {
            return MypageTeacherDto.builder()
                    .name(user.getTeacherDetail().getName())
                    .role(user.getRole())
                    .email(user.getTeacherDetail().getEmail())
                    .phoneNumber(user.getTeacherDetail().getPhoneNumber())
                    .schoolName(user.getTeacherDetail().getSchoolName())
                    .subject(user.getTeacherDetail().getSubject())
                    .officeNumber(user.getTeacherDetail().getOfficeNumber())
                    .build();
        } else if ("professor".equals(user.getRole())) {
            return MypageProfessorDto.builder()
                    .name(user.getProfessorDetail().getName())
                    .role(user.getRole())
                    .college(user.getProfessorDetail().getCollege())
                    .department(user.getProfessorDetail().getDepartment())
                    .email(user.getProfessorDetail().getEmail())
                    .phoneNumber(user.getProfessorDetail().getPhoneNumber())
                    .officeNumber(user.getProfessorDetail().getOfficeNumber())
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }
    }

    
    @Transactional
    public void updateUserInfo(MypageResponseDto userInfoDto) {
    	User user = userRepository.findById(userInfoDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userInfoDto.getId()));

        switch (user.getRole()) {
            case "student":
                if (userInfoDto instanceof MypageStudentDto) {
                    MypageStudentDto studentDto = (MypageStudentDto) userInfoDto;
                    user.getStudentDetail().setName(studentDto.getName());
                    user.getStudentDetail().setCollege(studentDto.getCollege());
                    user.getStudentDetail().setDepartment(studentDto.getDepartment());
                    user.getStudentDetail().setEmail(studentDto.getEmail());
                    user.getStudentDetail().setPhoneNumber(studentDto.getPhoneNumber());
                    user.getStudentDetail().setSchoolName(studentDto.getSchoolName());
                    user.getStudentDetail().setStudentNumber(studentDto.getStudentNumber());
                    user.getStudentDetail().setSubject(studentDto.getSubject());
                }
                break;
            case "teacher":
                if (userInfoDto instanceof MypageTeacherDto) {
                    MypageTeacherDto teacherDto = (MypageTeacherDto) userInfoDto;
                    user.getTeacherDetail().setName(teacherDto.getName());
                    user.getTeacherDetail().setEmail(teacherDto.getEmail());
                    user.getTeacherDetail().setPhoneNumber(teacherDto.getPhoneNumber());
                    user.getTeacherDetail().setSchoolName(teacherDto.getSchoolName());
                    user.getTeacherDetail().setSubject(teacherDto.getSubject());
                    user.getTeacherDetail().setOfficeNumber(teacherDto.getOfficeNumber());
                }
                break;
            case "professor":
                if (userInfoDto instanceof MypageProfessorDto) {
                    MypageProfessorDto professorDto = (MypageProfessorDto) userInfoDto;
                    user.getProfessorDetail().setName(professorDto.getName());
                    user.getProfessorDetail().setCollege(professorDto.getCollege());
                    user.getProfessorDetail().setDepartment(professorDto.getDepartment());
                    user.getProfessorDetail().setEmail(professorDto.getEmail());
                    user.getProfessorDetail().setPhoneNumber(professorDto.getPhoneNumber());
                    user.getProfessorDetail().setOfficeNumber(professorDto.getOfficeNumber());
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }

        userRepository.save(user); // 변경 사항 저장
    }
}