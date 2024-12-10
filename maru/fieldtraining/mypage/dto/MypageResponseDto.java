package com.fieldtraining.mypage.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, // JSON에 클래스 정보를 명시적으로 추가
		include = JsonTypeInfo.As.PROPERTY,
		property = "role" // role 값을 기준으로 적절한 하위 클래스 선택
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = MypageStudentDto.class, name = "student"),
	@JsonSubTypes.Type(value = MypageTeacherDto.class, name = "teacher"),
	@JsonSubTypes.Type(value = MypageProfessorDto.class, name = "professor")
})
public interface MypageResponseDto {
	Long getId();
	String getName();
}