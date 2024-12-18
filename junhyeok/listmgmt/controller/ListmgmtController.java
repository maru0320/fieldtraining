package com.fieldtraining.listmgmt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.listmgmt.dto.ProfessorDto;
import com.fieldtraining.listmgmt.dto.StudentDto;
import com.fieldtraining.listmgmt.dto.TeacherDto;
import com.fieldtraining.listmgmt.service.ListmgmtService;

@RestController
@RequestMapping("/api/listmanagement")
@CrossOrigin(origins = "http://localhost:3000")
public class ListmgmtController {
	private final ListmgmtService listmgmtService;
	
	public ListmgmtController(ListmgmtService listmgmtService) {
		this.listmgmtService = listmgmtService;
	}
	
	//학생 리스트 출력
	@GetMapping("/students")
	public ResponseEntity<List<StudentDto>> getStudents() {
		return ResponseEntity.ok(listmgmtService.getStudents());
	}

	//교사 리스트 출력
	@GetMapping("/teachers")
	public ResponseEntity<List<TeacherDto>> getTeachers() {
		return ResponseEntity.ok(listmgmtService.getTeachers());
	}
	
	//학과로 학생 검색
	@GetMapping("/searchByStudent")
	public ResponseEntity<List<StudentDto>> searchByDepartmentStudent(@RequestParam String department) {
		return ResponseEntity.ok(listmgmtService.searchByDepartmentStudent(department));
	}
	
	//학과로 교수 검색
	@GetMapping("/searchByProfessor")
	public ResponseEntity<List<ProfessorDto>> searchByDepartmentProfessor(@RequestParam String department){
		return ResponseEntity.ok(listmgmtService.searchByDepartmentProfessor(department));
	}
	
	// 교사에 매칭된 학생 조회
    @GetMapping("/studentsForTeacher/{teacherId}")
    public ResponseEntity<List<StudentDto>> getStudentsForTeacher(@PathVariable Long teacherId) {
        List<StudentDto> students = listmgmtService.getStudentsForTeacher(teacherId);
        return ResponseEntity.ok(students);
    }
	
	// 특정 사용자의 schoolName으로 교사 목록 가져오기
	@GetMapping("/teachersByUserSchool/{userId}")
    public ResponseEntity<List<TeacherDto>> getTeachersByUserSchool(@PathVariable Long userId) {
        List<TeacherDto> teachers = listmgmtService.getTeachersByUserSchool(userId);
        return ResponseEntity.ok(teachers);
    }
}
