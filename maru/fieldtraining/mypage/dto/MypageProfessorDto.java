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
public class MypageProfessorDto implements MypageResponseDto {
	
	private Long id;
    private String name;
    private String role;
    private String college;
    private String department;
    private String email;
    private String phoneNumber;
    private String officeNumber;
}
