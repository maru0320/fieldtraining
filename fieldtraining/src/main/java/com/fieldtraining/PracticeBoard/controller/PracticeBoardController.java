package com.fieldtraining.PracticeBoard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.PracticeBoard.dto.PracticeBoardDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardResponseDto;
import com.fieldtraining.PracticeBoard.dto.SearchDto;
import com.fieldtraining.PracticeBoard.service.PracticeBoardService;

@RequestMapping("/practice")
@RestController
public class PracticeBoardController {
	
	@Autowired
	PracticeBoardService practiceBoardService;

	// 글쓰기 
	@PostMapping("/write")
	public PracticeBoardResponseDto createBoard(@RequestBody PracticeBoardDto practiceBoardDto) {
	    return practiceBoardService.createBoard(practiceBoardDto);
	}

	// 검색
	@PostMapping("/search")
	public List<PracticeBoardResponseDto> searchBoard(@RequestBody SearchDto searchDto) {
	    return practiceBoardService.searchBoard(searchDto);
	}
	
	// 게시판 목록
	@GetMapping("/list")
	public List<PracticeBoardResponseDto> practiceBoardList(){
		return practiceBoardService.practiceBoardList();
	}

}
