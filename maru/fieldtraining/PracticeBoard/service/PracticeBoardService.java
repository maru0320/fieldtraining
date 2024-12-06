package com.fieldtraining.PracticeBoard.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.PracticeBoard.dto.PracticeBoardDetailDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardResponseDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardUpdateDto;
import com.fieldtraining.PracticeBoard.dto.PracticeFileDto;
import com.fieldtraining.PracticeBoard.dto.SearchDto;
import com.fieldtraining.PracticeBoard.entity.PracticeBoard;
import com.fieldtraining.PracticeBoard.entity.PracticeFile;
import com.fieldtraining.PracticeBoard.repository.PracticeBoardRepository;
import com.fieldtraining.PracticeBoard.repository.PracticeFileRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PracticeBoardService {

	@Autowired
	PracticeBoardRepository practiceBoardRepository;

	@Autowired
	PracticeFileRepository practicefileRepository;

	@Autowired
	UserRepository userRepository;

	// 게시판 쓰기
	public PracticeBoardResponseDto createBoard(PracticeBoardDto practiceBoardDto, List<MultipartFile> files) throws IOException{
		PracticeBoard practiceBoard = dtoToEntity(practiceBoardDto);
		practiceBoard = practiceBoardRepository.save(practiceBoard);

		if(files != null && !files.isEmpty()) {
			List<PracticeFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				PracticeFile fileEntity = PracticeFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.practiceBoard(practiceBoard)
						.build();
				fileEntities.add(fileEntity);
			}
			practicefileRepository.saveAll(fileEntities);
		}

		return EntityToDto(practiceBoard);
	}

	private String saveFileToServer(MultipartFile file) throws IOException {
		// 서버에 파일 저장 (예: "uploads/" 디렉토리에 저장)
		String uploadDir = "c:/testfile/";
		String storedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path path = Paths.get(uploadDir + storedName);
		Files.createDirectories(path.getParent()); // 디렉토리 생성
		file.transferTo(path.toFile()); // 파일 저장
		return path.toString();
	}

	// 게시판 목록 
	public List<PracticeBoardResponseDto> practiceBoardList() {
		List<PracticeBoard> boards = practiceBoardRepository.findAll();

		return boards.stream()
				.map(this::EntityToDto)
				.collect(Collectors.toList());
	}

	// 게시판 검색 
	public List<PracticeBoardResponseDto> searchBoard(SearchDto searchdto) {
		List<PracticeBoard> boards = practiceBoardRepository.searchByKeyword(searchdto.getKeyword());

		return boards.stream()
				.map(this::EntityToDto)
				.collect(Collectors.toList());
	}

	// 게시판 상세정보
	public PracticeBoardDetailDto practiceBoardDetail(Long boardID) {
		PracticeBoard board = practiceBoardRepository.findById(boardID)
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + boardID));


		List<PracticeFileDto> fileDtos = board.getFiles().stream()
				.map(file -> PracticeFileDto.builder()
						.id(file.getId())
						.originalName(file.getOriginalName())
						.storedPath(file.getStoredPath())
						.fileSize(file.getFileSize())
						.uploadTime(file.getUploadTime())
						.build())
				.collect(Collectors.toList());

		return PracticeBoardDetailDto.builder()
				.boardID(board.getBoardID())
				.title(board.getTitle())
				.content(board.getContent())
				.writerName(board.getWriterName())
				.trainingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.files(fileDtos)
				.build();
	}

	// 게시판 수정
	public void updateBoard(PracticeBoardUpdateDto updateDto, List<MultipartFile> files) throws IOException {
		if (updateDto.getBoardID() == 0) {
			throw new IllegalArgumentException("잘못된 게시판 ID 값입니다.");
		}

		PracticeBoard updateboard = practiceBoardRepository.findById(updateDto.getBoardID())
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + updateDto.getBoardID()));

		updateboard.setTitle(updateDto.getTitle());
		updateboard.setContent(updateDto.getContent());
		
		if(files != null && !files.isEmpty()) {
			List<PracticeFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				PracticeFile fileEntity = PracticeFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.practiceBoard(updateboard)
						.build();
				fileEntities.add(fileEntity);
			}
			practicefileRepository.saveAll(fileEntities);
		}
		
	}
	
	
	// 글 삭제
	public void practiceBoardDelete(Long boardID) {
		PracticeBoard board = practiceBoardRepository.findById(boardID)
				.orElseThrow(() -> new RuntimeException("게시글 찾을 수 없음"));

		practiceBoardRepository.delete(board);
	}

	// 파일 삭제
	@GetMapping("/update/fileDelete")
	public void fileDelete(Long fileID) {
		PracticeFile file = practicefileRepository.findById(fileID)
				.orElseThrow(() -> new RuntimeException("파일 찾을 수 없음"));
		practicefileRepository.delete(file);
	}	



	// requsestdto를 entity로 변환하는 함수
	public PracticeBoard dtoToEntity(PracticeBoardDto dto) {
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
				.content(dto.getContent())
				.trainingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
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
