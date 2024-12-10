package com.fieldtraining.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.mypage.dto.MypageProfessorDto;
import com.fieldtraining.mypage.dto.MypageResponseDto;
import com.fieldtraining.mypage.dto.MypageStudentDto;
import com.fieldtraining.mypage.dto.MypageTeacherDto;
import com.fieldtraining.mypage.service.MypageService;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @GetMapping("/userinfo")
    public ResponseEntity<MypageResponseDto> userInfo(@RequestParam Long id) {
        MypageResponseDto userInfo = mypageService.userInfo(id);
        return ResponseEntity.ok(userInfo);
    }
    
    @PostMapping("/update")
    public ResponseEntity<Void> updateUserInfo(@RequestBody MypageResponseDto userInfoDto) {
        switch (userInfoDto.getClass().getSimpleName()) {
            case "MypageStudentDto":
                mypageService.updateUserInfo((MypageStudentDto) userInfoDto);
                break;
            case "MypageTeacherDto":
                mypageService.updateUserInfo((MypageTeacherDto) userInfoDto);
                break;
            case "MypageProfessorDto":
                mypageService.updateUserInfo((MypageProfessorDto) userInfoDto);
                break;
            default:
                throw new IllegalArgumentException("Unsupported role type");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}