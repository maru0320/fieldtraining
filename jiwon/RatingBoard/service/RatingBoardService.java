package com.fieldtraining.RatingBoard.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.RatingBoard.dto.CommentRequestDto;
import com.fieldtraining.RatingBoard.dto.CommentResponseDto;
import com.fieldtraining.RatingBoard.dto.RatingBoardDetailDto;
import com.fieldtraining.RatingBoard.dto.RatingBoardRequestDto;
import com.fieldtraining.RatingBoard.dto.RatingBoardResponseDto;
import com.fieldtraining.RatingBoard.dto.RatingBoardUpdateDto;
import com.fieldtraining.RatingBoard.dto.RatingFileDto;
import com.fieldtraining.RatingBoard.entity.RatingBoard;
import com.fieldtraining.RatingBoard.entity.RatingComment;
import com.fieldtraining.RatingBoard.entity.RatingFile;
import com.fieldtraining.RatingBoard.repository.RatingBoardRepository;
import com.fieldtraining.RatingBoard.repository.RatingCommentRepository;
import com.fieldtraining.RatingBoard.repository.RatingFileRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.matching.repository.MatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class RatingBoardService {

	@Autowired
	RatingBoardRepository ratingBoardRepository;

	@Autowired
	RatingFileRepository ratingFileRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	RatingCommentRepository commentRepository;


	// 게시판 쓰기
	public RatingBoardResponseDto createBoard(RatingBoardRequestDto RatingBoardRequestDto, List<MultipartFile> files) throws IOException{
		RatingBoard ratingBoard = dtoToEntity(RatingBoardRequestDto);
		ratingBoard = ratingBoardRepository.save(ratingBoard);

		if(files != null && !files.isEmpty()) {
			List<RatingFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				RatingFile fileEntity = RatingFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.ratingBoard(ratingBoard)
						.build();
				fileEntities.add(fileEntity);
			}
			ratingFileRepository.saveAll(fileEntities);
		}

		return EntityToDto(ratingBoard);
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
	public RatingBoard dtoToEntity(RatingBoardRequestDto dto) {
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
		}

		// RatingBoard 객체 생성 및 반환
		return RatingBoard.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.writer(writer) // User 객체 설정
				.writerName(writerName) // 작성자 이름 설정
				.build();
	}


	// 게시글 entity를 ResponseDto 로 변환하는 함수
	public RatingBoardResponseDto EntityToDto(RatingBoard entity) {
		return RatingBoardResponseDto.builder()
				.boardID(entity.getBoardID())
				.title(entity.getTitle())
				.writerName(entity.getWriterName())
				.date(entity.getDate())
				.build();
	}


	// 게시글 목록
	public Page<RatingBoardResponseDto> getBoardsByRole(Long userId, int page, int size, String keyword) {
		// 사용자 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

		// 페이징 정보 설정
		Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
		Page<RatingBoard> boards;

		// 역할에 따라 데이터 조회
		if ("student".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? ratingBoardRepository.findBoardsForStudent(userId, pageable)
							: ratingBoardRepository.findBoardsForStudentWithKeyword(userId, keyword, pageable);
		} else if ("teacher".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? ratingBoardRepository.findBoardsForTeacher(userId, pageable)
							: ratingBoardRepository.findBoardsForTeacherWithKeyword(userId, keyword, pageable);
		} else if ("professor".equals(user.getRole())) {
			String college = user.getProfessorDetail().getCollege();
			String department = user.getProfessorDetail().getDepartment();
			boards = (keyword == null || keyword.isEmpty())
					? ratingBoardRepository.findBoardsForProfessor(college, department, pageable)
							: ratingBoardRepository.findBoardsForProfessorWithKeyword(college, department, keyword, pageable);
		} else if ("admin".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? ratingBoardRepository.findBoardsForAdmin(pageable)
							: ratingBoardRepository.findBoardsForAdminWithKeyword(keyword, pageable);
		} else {
			throw new IllegalArgumentException("유효하지 않은 Role입니다: " + user.getRole());
		}

		// Page 객체를 DTO로 변환하여 반환
		return boards.map(this::EntityToDto);
	}

	// 게시판 상세정보
	public RatingBoardDetailDto RatingBoardDetail(Long boardID) {
		RatingBoard board = ratingBoardRepository.findById(boardID)
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + boardID));

		List<RatingFileDto> fileDtos = board.getFiles().stream()
				.map(file -> RatingFileDto.builder()
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

		return RatingBoardDetailDto.builder()
				.boardID(board.getBoardID())
				.writerID(board.getWriter().getId())
				.title(board.getTitle())
				.content(board.getContent())
				.writerName(board.getWriterName())
				.date(board.getDate())
				.files(fileDtos)
				.comments(CommentResponseDtos)
				.build();
	}

	// 게시판 수정
	public void updateBoard(RatingBoardUpdateDto updateDto, List<MultipartFile> files) throws IOException {
		if (updateDto.getBoardID() == 0) {
			throw new IllegalArgumentException("잘못된 게시판 ID 값입니다.");
		}

		RatingBoard updateboard = ratingBoardRepository.findById(updateDto.getBoardID())
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + updateDto.getBoardID()));

		updateboard.setTitle(updateDto.getTitle());
		updateboard.setContent(updateDto.getContent());

		if(files != null && !files.isEmpty()) {
			List<RatingFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				RatingFile fileEntity = RatingFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.ratingBoard(updateboard)
						.build();
				fileEntities.add(fileEntity);
			}
			ratingFileRepository.saveAll(fileEntities);
		}

	}

	// 게시글 삭제
	public void RatingBoardDelete(Long boardID) {
		ratingBoardRepository.deleteById(boardID);
	}

	// 게시글 파일 삭제
	public void fileDelete(Long fileID) {
		ratingFileRepository.deleteById(fileID);
	}	

	// 댓글 생성
	public void createComment(CommentRequestDto dto) {
		RatingComment comment = commentDtoToEntity(dto);
		RatingBoard RatingBoard = comment.getRatingBoard();
	    
	    if (RatingBoard != null) {
	        RatingBoard.addComment(comment); // RatingBoard에 댓글 추가
	    }
	    
		commentRepository.save(comment);
	}

	// 댓글 RequestDto를 entity로 변환
	public RatingComment commentDtoToEntity(CommentRequestDto dto) {
		User user = userRepository.findById(dto.getUserID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserID())); 

		RatingBoard ratingBoard = ratingBoardRepository.findById(dto.getBoardID())
				.orElseThrow(() -> new RuntimeException("RatingBoard not found with id: " + dto.getBoardID()));
	
		
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

		return RatingComment.builder()
				.content(dto.getContent())
				.ratingBoard(ratingBoard)
				.user(user)
				.writerName(writerName)
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();
	}
	

	// 댓글 entity를 ResponseDto로변환
	public CommentResponseDto commentEntityToDto(RatingComment entity) {

		return CommentResponseDto.builder()
				.content(entity.getContent())
				.date(entity.getDate())
				.commentID(entity.getCommentID())
				.userID(entity.getUser().getId())
				.writerName(entity.getWriterName())
				.build();
	}
	
	// 댓글 삭제
	public void commentDelete(Long commentID) {
		commentRepository.deleteById(commentID);	
	}

}
