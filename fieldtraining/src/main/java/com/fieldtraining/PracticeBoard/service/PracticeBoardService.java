package com.fieldtraining.PracticeBoard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.PracticeBoard.dto.PracticeBoardDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardResponseDto;
import com.fieldtraining.PracticeBoard.entity.PracticeBoard;
import com.fieldtraining.PracticeBoard.repository.PracticeBoardRepository;

@Service
public class PracticeBoardService {
	
	@Autowired
	PracticeBoardRepository practiceBoardRepository;
	
	public PracticeBoardResponseDto createBoard(PracticeBoardDto practiceBoardDto) {
		PracticeBoard practiceBoard = convertToEntity(practiceBoardDto);
		PracticeBoard saveBoard = practiceBoardRepository.save(practiceBoard);
		return convertToResponseDto(saveBoard);
	};
	
	public PracticeBoard convertToEntity(practiceBoardDto dto){
		PracticeBoard practiceBoard = new PracticeBoard();
		practiceBoardDto
	}
	
	

}
