package com.packt.fieldtraining.ManagerInfo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.fieldtraining.ManagerInfo.dto.ManagerInfoDto;
import com.packt.fieldtraining.ManagerInfo.dto.ManagerInfoUpdateDto;
import com.packt.fieldtraining.ManagerInfo.service.ManagerInfoService;

@RestController
@RequestMapping("/api/managerInfo")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerInfoController {
	
	private final ManagerInfoService managerInfoService;
	
	public ManagerInfoController(ManagerInfoService managerInfoService) {
		this.managerInfoService = managerInfoService;
	}
	
	@GetMapping("/manager/{id}")
	public ResponseEntity<ManagerInfoDto> getManagerInfo(@PathVariable Long id) {
        // 서비스에서 매니저 정보를 가져오고, DTO로 변환한 후 반환
        ManagerInfoDto managerInfo = managerInfoService.getManagerInfo(id);
        return ResponseEntity.ok(managerInfo);
    }
	
	//정보 수정
}
