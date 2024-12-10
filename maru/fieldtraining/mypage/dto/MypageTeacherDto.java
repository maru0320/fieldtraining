package com.fieldtraining.mypage.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MypageTeacherDto implements MypageResponseDto {
	
	private Long id;
    private String name;
    private String role;
    private String email;
    private String phoneNumber;
    private String schoolName;
    private String subject;
    private String officeNumber;
}
