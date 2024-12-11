package com.fieldtraining.managerInfo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.managerInfo.dto.CollegeUpdateDto;
import com.fieldtraining.managerInfo.dto.SchoolUpdateDto;
import com.fieldtraining.managerInfo.service.ManagerInfoService;

@RestController
@RequestMapping("/api/managerInfo")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerInfoController {
	
    private ManagerInfoService managerInfoService;
    
    public ManagerInfoController(ManagerInfoService managerInfoService) {
    	this.managerInfoService = managerInfoService;
    }

    @GetMapping("/college/{id}")
    public ResponseEntity<?> getCollegeManagerInfo(@PathVariable Long id) {
        Object managerInfo = managerInfoService.getManagerInfoById(id, "collegeManager");

        if (managerInfo != null) {
            return ResponseEntity.ok(managerInfo);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/school/{id}")
    public ResponseEntity<?> getSchoolManagerInfo(@PathVariable Long id) {
        Object managerInfo = managerInfoService.getManagerInfoById(id, "schoolManager");

        if (managerInfo != null) {
            return ResponseEntity.ok(managerInfo);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    
    // 학교 관리자 정보 업데이트
    @PutMapping("/school/{id}")
    public ResponseEntity<String> updateSchoolManagerInfo(
            @PathVariable Long id,
            @RequestBody SchoolUpdateDto schoolUpdateDto) {
        managerInfoService.updateSchoolManagerInfo(id, schoolUpdateDto);
        return ResponseEntity.ok("학교 관리자 정보가 업데이트되었습니다.");
    }

    // 대학 관리자 정보 업데이트
    @PutMapping("/college/{id}")
    public ResponseEntity<String> updateCollegeManagerInfo(
            @PathVariable Long id,
            @RequestBody CollegeUpdateDto collegeUpdateDto) {
        managerInfoService.updateCollegeManagerInfo(id, collegeUpdateDto);
        return ResponseEntity.ok("대학 관리자 정보가 업데이트되었습니다.");
    }
}
