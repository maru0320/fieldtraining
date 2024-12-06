package com.fieldtraining.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.mypage.dto.MypageStudentDto;
import com.fieldtraining.mypage.service.MypageService;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @GetMapping("/user")
    public MypageStudentDto getUserInfo(@RequestParam Long id) {
        return  mypageService.getUserInfo(id);
    }

}