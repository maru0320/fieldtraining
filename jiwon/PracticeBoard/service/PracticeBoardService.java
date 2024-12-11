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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.PracticeBoard.dto.CommentRequestDto;
import com.fieldtraining.PracticeBoard.dto.CommentResponseDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardDetailDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardRequestDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardResponseDto;
import com.fieldtraining.PracticeBoard.dto.PracticeBoardUpdateDto;
import com.fieldtraining.PracticeBoard.dto.PracticeFileDto;
import com.fieldtraining.PracticeBoard.entity.PracticeComment;
import com.fieldtraining.PracticeBoard.entity.PracticeBoard;
import com.fieldtraining.PracticeBoard.entity.PracticeFile;
import com.fieldtraining.PracticeBoard.repository.CommentRepository;
import com.fieldtraining.PracticeBoard.repository.PracticeBoardRepository;
import com.fieldtraining.PracticeBoard.repository.PracticeFileRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.repository.MatchRepository;

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

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	CommentRepository commentRepository;


	// 게시판 쓰기
	public PracticeBoardResponseDto createBoard(PracticeBoardRequestDto practiceBoardRequestDto, List<MultipartFile> files) throws IOException{
		PracticeBoard practiceBoard = dtoToEntity(practiceBoardRequestDto);
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

	// 서버에 파일 저장 (예: "uploads/" 디렉토리에 저장)
	private String saveFileToServer(MultipartFile file) throws IOException {
		String uploadDir = "c:/testfile/";
		String storedName = file.getOriginalFilename();
		Path path = Paths.get(uploadDir + storedName);
		Files.createDirectories(path.getParent()); // 디렉토리 생성
		file.transferTo(path.toFile()); // 파일 저장
		return path.toString();
	}

	// 게시글 RequsestDto를 entity로 변환하는 함수
	public PracticeBoard dtoToEntity(PracticeBoardRequestDto dto) {
		User writer = userRepository.findById(dto.getWriterID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getWriterID()));

		// 작성자 이름 설정
		String writerName = null;
		if ("student".equals(writer.getRole())) {
			writerName = writer.getStudentDetail() != null ? writer.getStudentDetail().getName() : null;
		} else if ("teacher".equals(writer.getRole())) {
			writerName = writer.getTeacherDetail() != null ? writer.getTeacherDetail().getName() : null;
		} else if ("professor".equals(writer.getRole())) {
			writerName = writer.getProfessorDetail() != null ? writer.getProfessorDetail().getName() : null;
		}  else if ("admin".equals(writer.getRole())) {
			writerName = "관리자";
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


	// 게시글 entity를 ResponseDto 로 변환하는 함수
	public PracticeBoardResponseDto EntityToDto(PracticeBoard entity) {
		return PracticeBoardResponseDto.builder()
				.boardID(entity.getBoardID())
				.title(entity.getTitle())
				.writerName(entity.getWriterName())
				.trainingDate(entity.getTrainingDate())
				.build();
	}


	// 게시글 목록
	public Page<PracticeBoardResponseDto> getBoardsByRole(Long userId, int page, int size, String keyword) {
		// 사용자 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

		// 페이징 정보 설정
		Pageable pageable = PageRequest.of(page, size, Sort.by("boardID").descending());
		Page<PracticeBoard> boards;

		// 역할에 따라 데이터 조회
		if ("student".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? practiceBoardRepository.findBoardsForStudent(userId, pageable)
							: practiceBoardRepository.findBoardsForStudentWithKeyword(userId, keyword, pageable);
		} else if ("teacher".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? practiceBoardRepository.findBoardsForTeacher(userId, pageable)
							: practiceBoardRepository.findBoardsForTeacherWithKeyword(userId, keyword, pageable);
		} else if ("professor".equals(user.getRole())) {
			String college = user.getProfessorDetail().getCollege();
			String department = user.getProfessorDetail().getDepartment();
			boards = (keyword == null || keyword.isEmpty())
					? practiceBoardRepository.findBoardsForProfessor(college, department, pageable)
							: practiceBoardRepository.findBoardsForProfessorWithKeyword(college, department, keyword, pageable);
		} else if ("admin".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? practiceBoardRepository.findBoardsForAdmin(pageable)
							: practiceBoardRepository.findBoardsForAdminWithKeyword(keyword, pageable);
		} else {
			throw new IllegalArgumentException("유효하지 않은 Role입니다: " + user.getRole());
		}

		// Page 객체를 DTO로 변환하여 반환
		return boards.map(this::EntityToDto);
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

		List<CommentResponseDto> CommentResponseDtos = board.getComments().stream()
				.map(comment -> CommentResponseDto.builder()
						.commentID(comment.getCommentID())
						.userID(comment.getUser().getId())
						.content(comment.getContent())
						.date(comment.getDate())
						.writerName(comment.getWriterName())
						.build())
				.collect(Collectors.toList());

		return PracticeBoardDetailDto.builder()
				.boardID(board.getBoardID())
				.writerID(board.getWriter().getId())
				.title(board.getTitle())
				.content(board.getContent())
				.writerName(board.getWriterName())
				.trainingDate(board.getTrainingDate())
				.files(fileDtos)
				.comments(CommentResponseDtos)
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

	// 게시글 삭제
	public void practiceBoardDelete(Long boardID) {
		practiceBoardRepository.deleteById(boardID);
	}

	// 게시글 파일 삭제
	public void fileDelete(Long fileID) {
		PracticeFile file = practicefileRepository.findById(fileID)
				.orElseThrow(() -> new RuntimeException("파일 찾을 수 없음"));
		practicefileRepository.delete(file);
	}	

	// 댓글 생성
	public void createComment(CommentRequestDto dto) {
		PracticeComment comment = commentDtoToEntity(dto);
		PracticeBoard practiceBoard = comment.getPracticeBoard();

		if (practiceBoard != null) {
			practiceBoard.addComment(comment); // PracticeBoard에 댓글 추가
		}
		commentRepository.save(comment);
	}

	// 댓글 RequestDto를 entity로 변환
	public PracticeComment commentDtoToEntity(CommentRequestDto dto) {
		User user = userRepository.findById(dto.getUserID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserID())); 

		PracticeBoard practiceBoard = practiceBoardRepository.findById(dto.getBoardID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getBoardID()));


		String writerName = null;
		if ("student".equals(user.getRole())) {
			writerName = user.getStudentDetail().getName();
		} else if ("teacher".equals(user.getRole())) {
			writerName = user.getTeacherDetail().getName();
		} else if ("professor".equals(user.getRole())) {
			writerName = user.getProfessorDetail().getName();
		} else if ("admin".equals(user.getRole())) {
			writerName = "관리자";
		}

		return PracticeComment.builder()
				.content(dto.getContent())
				.practiceBoard(practiceBoard)
				.user(user)
				.writerName(writerName)
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();
	}


	// 댓글 entity를 ResponseDto로변환
	public CommentResponseDto commentEntityToDto(PracticeComment entity) {

		return CommentResponseDto.builder()
				.content(entity.getContent())
				.date(entity.getDate())
				.commentID(entity.getCommentID())
				.userID(entity.getUser().getId())
				.writerName(entity.getWriterName())
				.build();
	}

	@GetMapping("/detail/delete")
	public void commentDelete(Long commentID) {
		commentRepository.deleteById(commentID);	
	}



}
