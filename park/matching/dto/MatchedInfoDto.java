package com.fieldtraining.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchedInfoDto {
	private Long id;
	// 학생 정보
    private String studentName;
    private String studentSchool;
    private String studentSubject;
    private String studentEmail;
    private String studentCollege;
    private String studentDepartment;
    private String studentPhoneNumber;

    // 선생님 정보
    private String teacherName;
    private String teacherSchool;
    private String teacherSubject;
    private String teacherEmail;
    private String teacherPhoneNumber;
    private String teacherOfficeNumber;
}
