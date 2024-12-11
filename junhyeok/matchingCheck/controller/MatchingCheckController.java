package com.fieldtraining.matchingCheck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.matchingCheck.dto.MatchingDto;
import com.fieldtraining.matchingCheck.service.MatchingCheckService;

@RestController
@RequestMapping("/api/matchingcheck")
@CrossOrigin(origins = "http://localhost:3000")
public class MatchingCheckController {
	@Autowired
    private MatchingCheckService matchingCheckService;
	
	@GetMapping("/match")
    public ResponseEntity<List<MatchingDto>> getAllMatches() {
        List<MatchingDto> matches = matchingCheckService.getAllMatches();
        return ResponseEntity.ok(matches);
    }
}
