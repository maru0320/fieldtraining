package com.packt.fieldtraining.matchingCheck.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.fieldtraining.matchingCheck.dto.MatchingDto;
import com.packt.fieldtraining.matchingCheck.service.MatchingCheckService;

@RestController
@RequestMapping("/api/matchingcheck")
@CrossOrigin(origins = "http://localhost:3000")
public class MatchingCheckController {
	private final MatchingCheckService matchingCheckService;
	
	public MatchingCheckController(MatchingCheckService matchingCheckService) {
		this.matchingCheckService = matchingCheckService;
	}
	
	@GetMapping("/match")
	public ResponseEntity<List<MatchingDto>> getAllMatches(){
		List<MatchingDto> matches = matchingCheckService.getAllMatches();
		return ResponseEntity.ok(matches);
	}
}
