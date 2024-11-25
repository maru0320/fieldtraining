package com.packt.fieldtraining.matching.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchResponseDto;
import com.packt.fieldtraining.matching.service.MatchingService;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

	private final MatchingService matchingService;
	
	public MatchingController(MatchingService matchingService) {
		this.matchingService = matchingService;
	}
	
	// 매칭 요청 처리
	@PostMapping("/request")
	public MatchResponseDto requestMatch(@RequestBody MatchRequestDto requestDto) {
		return matchingService.requestMatch(requestDto);
	}
	
	@PostMapping("/approve/{matchId}")
	public MatchResponseDto approveMatch(@PathVariable Long matchId) {
		return matchingService.approveMatch(matchId);
	}
}
