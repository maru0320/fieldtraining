package com.fieldtraining.listmgmt.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  // 기본 생성자 생성
public class StudentBySearchDto {
	private String studentName;
    private String studentCollege;
    private String studentDepartment;
    private String studentEmail;
    private String studentSubject;
    private int studentNumber;

    private List<TeacherDto> teachers;  // 매칭된 교사 리스트
    private String schoolName;  // 학교명
}
