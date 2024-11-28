package com.packt.fieldtraining.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.board.dto.PracticeBoardRequestDto;
import com.packt.fieldtraining.board.dto.PracticeBoardResponseDto;
import com.packt.fieldtraining.board.dto.SearchDto;
import com.packt.fieldtraining.board.entity.PracticeBoard;
import com.packt.fieldtraining.board.repository.PracticeBoardRepository;
import com.packt.fieldtraining.data.entity.User;
import com.packt.fieldtraining.data.repository.TeacherRepository;
import com.packt.fieldtraining.data.repository.UserRepository;

@Service
@Transactional
public class PracticeBoardService {

	@Autowired
	PracticeBoardRepository practiceBoardRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;

	// 글쓰기 기능
	public PracticeBoardResponseDto createBoard(PracticeBoardRequestDto PracticeBoardRequestDto) {
		PracticeBoard practiceBoard = dtoToEntity(PracticeBoardRequestDto);
		PracticeBoard saveBoard = practiceBoardRepository.save(practiceBoard);
		return EntityToDto(saveBoard);
	}
	
	// 검색 기능
	public List<PracticeBoardResponseDto> searchBoard(SearchDto searchdto) {
		List<PracticeBoard> boards = practiceBoardRepository.searchByKeyword(searchdto.getKeyword());
		
		return boards.stream()
					 .map(this::EntityToDto)
					 .collect(Collectors.toList());
	}
	
	// 글 목록 
	public List<PracticeBoardResponseDto> practiceBoardList() {
	    List<PracticeBoard> boards = practiceBoardRepository.findAll();

	    return boards.stream()
	                 .map(this::EntityToDto)
	                 .collect(Collectors.toList());
	}
	
	

	// requsestdto를 entity로 변환하는 함수
	public PracticeBoard dtoToEntity(PracticeBoardRequestDto dto) {
	    // User 객체 조회
	    User writer = userRepository.findById(dto.getWriterID())
	            .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getWriterID()));

	    // 작성자 이름 설정
	    String writerName = null;
	    if ("STUDENT".equals(writer.getRole())) {
	        writerName = writer.getStudentDetail() != null ? writer.getStudentDetail().getName() : null;
	    } else if ("TEACHER".equals(writer.getRole())) {
	        writerName = writer.getTeacherDetail() != null ? writer.getTeacherDetail().getName() : null;
	    } else if ("PROFESSOR".equals(writer.getRole())) {
	        writerName = writer.getProfessorDetail() != null ? writer.getProfessorDetail().getName() : null;
	    }

	    // PracticeBoard 객체 생성 및 반환
	    return PracticeBoard.builder()
	            .title(dto.getTitle())
	            .fileName(dto.getFileName())
	            .content(dto.getContent())
	            .trainingDate(LocalDateTime.now())
	            .writer(writer) // User 객체 설정
	            .writerName(writerName) // 작성자 이름 설정
	            .build();
	}


	// entity를 responsedto 로 변환하는 함수
	public PracticeBoardResponseDto EntityToDto(PracticeBoard entity) {
		return PracticeBoardResponseDto.builder()
				.boardID(entity.getBoardID())
				.title(entity.getTitle())
				.writerName(entity.getWriterName())
				.trainingDate(entity.getTrainingDate())
				.build();
	}

}