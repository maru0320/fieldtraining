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

import com.fieldtraining.managerInfo.dto.ManagerInfoDto;
import com.fieldtraining.managerInfo.service.ManagerInfoService;

@RestController
@RequestMapping("/api/managerInfo")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerInfoController {
	
    private ManagerInfoService managerInfoService;
    
    public ManagerInfoController(ManagerInfoService managerInfoService) {
    	this.managerInfoService = managerInfoService;
    }

    @GetMapping("/{role}/{userId}")
    public ResponseEntity<?> getManagerInfo(@PathVariable String role, @PathVariable String userId) {
        Object managerInfo = managerInfoService.getManagerInfo(userId, role);

        if (managerInfo != null) {
            return ResponseEntity.ok(managerInfo);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
 // 사용자 정보 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateManagerInfo(@RequestBody ManagerInfoDto managerInfoDto) {
        try {
            managerInfoService.updateManagerInfo(managerInfoDto);
            return ResponseEntity.ok("정보 수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정보 수정 중 오류 발생");
        }
    }
}
