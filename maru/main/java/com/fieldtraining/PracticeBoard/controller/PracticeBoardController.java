package com.fieldtraining.PracticeBoard.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.PracticeBoard.dto.PracticeBoardDetailDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardResponseDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardUpdateDto;
import com.fieldtraining.PracticeBoard.dto.SearchDto;
import com.fieldtraining.PracticeBoard.service.PracticeBoardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/practice")
@RequiredArgsConstructor
@RestController
public class PracticeBoardController {

	@Autowired
	PracticeBoardService practiceBoardService;

	//파일 포함 글쓰기
	@PostMapping(value = "/write", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })	
	public PracticeBoardResponseDto createBoard(
			@ModelAttribute PracticeBoardDto practiceBoardDto,
			@RequestParam(value = "files", required = false)  List<MultipartFile> files) throws IOException {
		return practiceBoardService.createBoard(practiceBoardDto, files);
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

	// 게시판 상세정보
	@GetMapping("/detail")
	public PracticeBoardDetailDto practiceBoardDetail(@RequestParam Long boardID ) {
		return practiceBoardService.practiceBoardDetail(boardID);
	}

	//게시판 수정
	@PostMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public void updateBoard(
			@ModelAttribute PracticeBoardUpdateDto updateDto,
			@RequestParam(value = "files", required = false)  List<MultipartFile> files) throws IOException{
		
		practiceBoardService.updateBoard(updateDto, files);
	}

	// 게시판 삭제
	@GetMapping("/delete")
	public void practiceBoardDelete(@RequestParam Long boardID) {
		practiceBoardService.practiceBoardDelete(boardID);
	}	

	// 파일 삭제
	@GetMapping("/update/fileDelete")
	public void fileDelete(@RequestParam Long fileID) {
		practiceBoardService.fileDelete(fileID);
	}	

}
