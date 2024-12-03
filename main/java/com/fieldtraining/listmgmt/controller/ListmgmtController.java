package com.fieldtraining.listmgmt.controller;

import java.util.List;

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
	
	// 실습생 목록 반환
    @GetMapping("/student")
    public List<StudentDto> getStudents() {
        return listmgmtService.getStudents();
    }

    // 교사 목록 반환
    @GetMapping("/teacher")
    public List<TeacherDto> getTeachers() {
        return listmgmtService.getTeachers();
    }
    
    //학과로 검색
    @GetMapping("/search")
    public List<StudentDto> searchByDepartment(@RequestParam String department){
    	return listmgmtService.searchByDepartment(department);
    }
}
