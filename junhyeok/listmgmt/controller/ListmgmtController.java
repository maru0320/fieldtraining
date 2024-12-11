package com.fieldtraining.listmgmt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/students")
	public ResponseEntity<List<StudentDto>> getStudents() {
		return ResponseEntity.ok(listmgmtService.getStudents());
	}

	@GetMapping("/teachers")
	public ResponseEntity<List<TeacherDto>> getTeachers() {
		return ResponseEntity.ok(listmgmtService.getTeachers());
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<StudentDto>> searchByDepartment(@RequestParam String department) {
		return ResponseEntity.ok(listmgmtService.searchByDepartment(department));
	}

	@GetMapping("/teachersForstudent")
	public ResponseEntity<List<TeacherDto>> getTeachersForStudent(@RequestParam Long studentId) {
		return ResponseEntity.ok(listmgmtService.getTeachersForStudent(studentId));
	}
}
