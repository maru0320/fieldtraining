package com.fieldtraining.mypage.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageTeacherDto implements MypageResponseDto {
    private String name;
    private String role;
    private String email;
    private String phoneNumber;
    private String schoolName;
    private String subject;
    private String officeNumber;
}
