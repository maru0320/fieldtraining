package com.fieldtraining.mypage.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageProfessorDto implements MypageResponseDto {
    private String name;
    private String role;
    private String college;
    private String department;
    private String email;
    private String phoneNumber;
    private String officeNumber;
}
