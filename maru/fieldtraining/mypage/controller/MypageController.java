package com.fieldtraining.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.mypage.dto.MypageResponseDto;
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

}