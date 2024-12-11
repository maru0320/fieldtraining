package com.fieldtraining.OperationBoard.service;

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

import com.fieldtraining.OperationBoard.dto.CommentRequestDto;
import com.fieldtraining.OperationBoard.dto.CommentResponseDto;
import com.fieldtraining.OperationBoard.dto.OperationBoardDetailDto;
import com.fieldtraining.OperationBoard.dto.OperationBoardRequestDto;
import com.fieldtraining.OperationBoard.dto.OperationBoardResponseDto;
import com.fieldtraining.OperationBoard.dto.OperationBoardUpdateDto;
import com.fieldtraining.OperationBoard.dto.OperationFileDto;
import com.fieldtraining.OperationBoard.entity.OperationBoard;
import com.fieldtraining.OperationBoard.entity.OperationComment;
import com.fieldtraining.OperationBoard.entity.OperationFile;
import com.fieldtraining.OperationBoard.repository.OperationBoardRepository;
import com.fieldtraining.OperationBoard.repository.OperationCommentRepository;
import com.fieldtraining.OperationBoard.repository.OperationFileRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.matching.repository.MatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class OperationBoardService {

	@Autowired
	OperationBoardRepository operationBoardRepository;

	@Autowired
	OperationFileRepository operationFileRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	OperationCommentRepository commentRepository;


	// 게시판 쓰기
	public OperationBoardResponseDto createBoard(OperationBoardRequestDto operationBoardRequestDto, List<MultipartFile> files) throws IOException{
		OperationBoard operationBoard = dtoToEntity(operationBoardRequestDto);
		operationBoard = operationBoardRepository.save(operationBoard);

		if(files != null && !files.isEmpty()) {
			List<OperationFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				OperationFile fileEntity = OperationFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.operationBoard(operationBoard)
						.build();
				fileEntities.add(fileEntity);
			}
			operationFileRepository.saveAll(fileEntities);
		}

		return EntityToDto(operationBoard);
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
	public OperationBoard dtoToEntity(OperationBoardRequestDto dto) {
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

		// operationBoard 객체 생성 및 반환
		return OperationBoard.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.writer(writer) // User 객체 설정
				.writerName(writerName) // 작성자 이름 설정
				.build();
	}


	// 게시글 entity를 ResponseDto 로 변환하는 함수
	public OperationBoardResponseDto EntityToDto(OperationBoard entity) {
		return OperationBoardResponseDto.builder()
				.boardID(entity.getBoardID())
				.title(entity.getTitle())
				.writerName(entity.getWriterName())
				.trainingDate(entity.getDate())
				.build();
	}


	// 게시글 목록
	public Page<OperationBoardResponseDto> getBoardsByRole(Long userId, int page, int size, String keyword) {
		// 사용자 조회
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

		// 페이징 정보 설정
		Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
		Page<OperationBoard> boards;

		// 역할에 따라 데이터 조회
		if ("student".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? operationBoardRepository.findBoardsForStudent(userId, pageable)
							: operationBoardRepository.findBoardsForStudentWithKeyword(userId, keyword, pageable);
		} else if ("teacher".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? operationBoardRepository.findBoardsForTeacher(userId, pageable)
							: operationBoardRepository.findBoardsForTeacherWithKeyword(userId, keyword, pageable);
		} else if ("professor".equals(user.getRole())) {
			String college = user.getProfessorDetail().getCollege();
			String department = user.getProfessorDetail().getDepartment();
			boards = (keyword == null || keyword.isEmpty())
					? operationBoardRepository.findBoardsForProfessor(college, department, pageable)
							: operationBoardRepository.findBoardsForProfessorWithKeyword(college, department, keyword, pageable);
		} else if ("admin".equals(user.getRole())) {
			boards = (keyword == null || keyword.isEmpty())
					? operationBoardRepository.findBoardsForAdmin(pageable)
							: operationBoardRepository.findBoardsForAdminWithKeyword(keyword, pageable);
		} else {
			throw new IllegalArgumentException("유효하지 않은 Role입니다: " + user.getRole());
		}

		// Page 객체를 DTO로 변환하여 반환
		return boards.map(this::EntityToDto);
	}

	// 게시판 상세정보
	public OperationBoardDetailDto operationBoardDetail(Long boardID) {
		OperationBoard board = operationBoardRepository.findById(boardID)
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + boardID));

		List<OperationFileDto> fileDtos = board.getFiles().stream()
				.map(file -> OperationFileDto.builder()
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

		return OperationBoardDetailDto.builder()
				.boardID(board.getBoardID())
				.writerID(board.getWriter().getId())
				.title(board.getTitle())
				.content(board.getContent())
				.writerName(board.getWriterName())
				.trainingDate(board.getDate())
				.files(fileDtos)
				.comments(CommentResponseDtos)
				.build();
	}

	// 게시판 수정
	public void updateBoard(OperationBoardUpdateDto updateDto, List<MultipartFile> files) throws IOException {
		if (updateDto.getBoardID() == 0) {
			throw new IllegalArgumentException("잘못된 게시판 ID 값입니다.");
		}

		OperationBoard updateboard = operationBoardRepository.findById(updateDto.getBoardID())
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + updateDto.getBoardID()));

		updateboard.setTitle(updateDto.getTitle());
		updateboard.setContent(updateDto.getContent());

		if(files != null && !files.isEmpty()) {
			List<OperationFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				OperationFile fileEntity = OperationFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.operationBoard(updateboard)
						.build();
				fileEntities.add(fileEntity);
			}
			operationFileRepository.saveAll(fileEntities);
		}

	}

	// 게시글 삭제
	public void operationBoardDelete(Long boardID) {
		operationBoardRepository.deleteById(boardID);
	}

	// 게시글 파일 삭제
	public void fileDelete(Long fileID) {
		operationFileRepository.deleteById(fileID);
	}	

	// 댓글 생성
	public void createComment(CommentRequestDto dto) {
		OperationComment comment = commentDtoToEntity(dto);
		OperationBoard operationBoard = comment.getOperationBoard();

		if (operationBoard != null) {
			operationBoard.addComment(comment); // operationBoard에 댓글 추가
		}

		commentRepository.save(comment);
	}

	// 댓글 RequestDto를 entity로 변환
	public OperationComment commentDtoToEntity(CommentRequestDto dto) {
		User user = userRepository.findById(dto.getUserID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserID())); 

		OperationBoard operationBoard = operationBoardRepository.findById(dto.getBoardID())
				.orElseThrow(() -> new RuntimeException("operationBoard not found with id: " + dto.getBoardID()));


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

		return OperationComment.builder()
				.content(dto.getContent())
				.operationBoard(operationBoard)
				.user(user)
				.writerName(writerName)
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();
	}


	// 댓글 entity를 ResponseDto로변환
	public CommentResponseDto commentEntityToDto(OperationComment entity) {

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
