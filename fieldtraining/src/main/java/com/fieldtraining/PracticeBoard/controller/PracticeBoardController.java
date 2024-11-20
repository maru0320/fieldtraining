package com.fieldtraining.PracticeBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fieldtraining.PracticeBoard.service.PracticeBoardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/practiceBoard")
@Controller
@RequiredArgsConstructor
public class PracticeBoardController {
	
	@Autowired
	PracticeBoardService practiceBoardService;
	
//	// 글쓰기 
//	@GetMapping("/write")
//    public PracticeBoardResponseDto createBoard(@RequestBody PracticeBoardDto practiceBoardDto) {
//        return practiceBoardService.createBoard(practiceBoardDto);
//    }
//    // 제목 또는 작성자로 게시글 검색
//    @GetMapping("/search")
//    public List<PracticeBoardResponseDto> searchBoards(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String writerName) {
//        return practiceBoardService.searchBoards(title, writerName);
//    

	// 게시글 생성 (POST 요청)
//    @PostMapping
//    public ResponseEntity<PracticeBoardResponseDto> createBoard(@RequestBody PracticeBoardDto practiceBoardDto) {
//        PracticeBoardResponseDto responseDto = practiceBoardService.createBoard(practiceBoardDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }
	
	
	
	
	
}
