package com.fieldtraining.ReferenceBoard.service;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.ReferenceBoard.dto.CommentRequestDto;
import com.fieldtraining.ReferenceBoard.dto.CommentResponseDto;
import com.fieldtraining.ReferenceBoard.dto.ReferenceBoardDetailDto;
import com.fieldtraining.ReferenceBoard.dto.ReferenceBoardRequestDto;
import com.fieldtraining.ReferenceBoard.dto.ReferenceBoardResponseDto;
import com.fieldtraining.ReferenceBoard.dto.ReferenceBoardUpdateDto;
import com.fieldtraining.ReferenceBoard.dto.ReferenceFileDto;
import com.fieldtraining.ReferenceBoard.entity.ReferenceBoard;
import com.fieldtraining.ReferenceBoard.entity.ReferenceComment;
import com.fieldtraining.ReferenceBoard.entity.ReferenceFile;
import com.fieldtraining.ReferenceBoard.repository.ReferenceBoardRepository;
import com.fieldtraining.ReferenceBoard.repository.ReferenceCommentRepository;
import com.fieldtraining.ReferenceBoard.repository.ReferenceFileRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.matching.repository.MatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReferenceBoardService {

	@Autowired
	ReferenceBoardRepository referenceBoardRepository;

	@Autowired
	ReferenceFileRepository referencefileRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	ReferenceCommentRepository commentRepository;


	// 게시판 쓰기
	public ReferenceBoardResponseDto createBoard(ReferenceBoardRequestDto referenceBoardRequestDto, List<MultipartFile> files) throws IOException{
		ReferenceBoard referenceBoard = dtoToEntity(referenceBoardRequestDto);
		referenceBoard = referenceBoardRepository.save(referenceBoard);

		if(files != null && !files.isEmpty()) {
			List<ReferenceFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				ReferenceFile fileEntity = ReferenceFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.referenceBoard(referenceBoard)
						.build();
				fileEntities.add(fileEntity);
			}
			referencefileRepository.saveAll(fileEntities);
		}

		return EntityToDto(referenceBoard);
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
	public ReferenceBoard dtoToEntity(ReferenceBoardRequestDto dto) {
		User writer = userRepository.findById(dto.getWriterID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getWriterID()));

		// 작성자 이름 설정
		String writerName = null;
		if ("schoolManager".equals(writer.getRole())) {
			writerName = writer.getSchoolManagerDetail().getSchoolName();
		} else if ("collegeManager".equals(writer.getRole())) {
			writerName = writer.getCollegeManagerDetail().getCollege();
		}

		// referenceBoard 객체 생성 및 반환
		return ReferenceBoard.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.writer(writer) // User 객체 설정
				.writerName(writerName) // 작성자 이름 설정
				.build();
	}


	// 게시글 entity를 ResponseDto 로 변환하는 함수
	public ReferenceBoardResponseDto EntityToDto(ReferenceBoard entity) {
		return ReferenceBoardResponseDto.builder()
				.boardID(entity.getBoardID())
				.title(entity.getTitle())
				.writerName(entity.getWriterName())
				.date(entity.getDate())
				.writerRole(entity.getWriter().getRole())
				.build();
	}


	// 게시글 목록
	public Page<ReferenceBoardResponseDto> getBoardsByRole(int page, int size, String keyword) {

		// 페이징 정보 설정
		Pageable pageable = PageRequest.of(page, size, Sort.by("boardID").descending());
		Page<ReferenceBoard> boards;

		boards = (keyword == null || keyword.isEmpty())
				? referenceBoardRepository.findBoardforManager(pageable)
						: referenceBoardRepository.findBoardforManagerWithKeyword(keyword, pageable);

		// Page 객체를 DTO로 변환하여 반환
		return boards.map(this::EntityToDto);
	}

	// 게시판 상세정보
	public ReferenceBoardDetailDto referenceBoardDetail(Long boardID) {
		ReferenceBoard board = referenceBoardRepository.findById(boardID)
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + boardID));

		List<ReferenceFileDto> fileDtos = board.getFiles().stream()
				.map(file -> ReferenceFileDto.builder()
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

		return ReferenceBoardDetailDto.builder()
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
	public void updateBoard(ReferenceBoardUpdateDto updateDto, List<MultipartFile> files) throws IOException {
		if (updateDto.getBoardID() == 0) {
			throw new IllegalArgumentException("잘못된 게시판 ID 값입니다.");
		}

		ReferenceBoard updateboard = referenceBoardRepository.findById(updateDto.getBoardID())
				.orElseThrow(() -> new EntityNotFoundException("게시글 찾을 수 없음" + updateDto.getBoardID()));

		updateboard.setTitle(updateDto.getTitle());
		updateboard.setContent(updateDto.getContent());

		if(files != null && !files.isEmpty()) {
			List<ReferenceFile> fileEntities = new ArrayList<>();
			for (MultipartFile file : files) {
				String storedPath = saveFileToServer(file); // 파일 저장 로직
				ReferenceFile fileEntity = ReferenceFile.builder()
						.originalName(file.getOriginalFilename())
						.storedPath(storedPath)
						.fileSize(file.getSize())
						.uploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.referenceBoard(updateboard)
						.build();
				fileEntities.add(fileEntity);
			}
			referencefileRepository.saveAll(fileEntities);
		}

	}

	// 게시글 삭제
	public void referenceBoardDelete(Long boardID) {
		referenceBoardRepository.deleteById(boardID);
	}

	// 게시글 파일 삭제
	public void fileDelete(Long fileID) {
		ReferenceFile file = referencefileRepository.findById(fileID)
				.orElseThrow(() -> new RuntimeException("파일 찾을 수 없음"));
		referencefileRepository.delete(file);
	}	

	// 댓글 생성
	public void createComment(CommentRequestDto dto) {
		ReferenceComment comment = commentDtoToEntity(dto);
		ReferenceBoard referenceBoard = comment.getReferenceBoard();

		if (referenceBoard != null) {
			referenceBoard.addComment(comment); // referenceBoard에 댓글 추가
		}
		commentRepository.save(comment);
	}

	// 댓글 RequestDto를 entity로 변환
	public ReferenceComment commentDtoToEntity(CommentRequestDto dto) {
		User writer = userRepository.findById(dto.getUserID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserID())); 

		ReferenceBoard referenceBoard = referenceBoardRepository.findById(dto.getBoardID())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getBoardID()));
		
		String writerName = null;
		if ("student".equals(writer.getRole())) {
			writerName = writer.getStudentDetail().getName();
		} else if ("teacher".equals(writer.getRole())) {
			writerName = writer.getTeacherDetail().getName();
		} else if ("professor".equals(writer.getRole())) {
			writerName = writer.getProfessorDetail().getName();
		} else if ("admin".equals(writer.getRole())) {
			writerName = "관리자";
		} else if ("schoolManager".equals(writer.getRole())) {
			writerName = writer.getSchoolManagerDetail().getSchoolName();
		} else if ("collegeManager".equals(writer.getRole())) {
			writerName = writer.getCollegeManagerDetail().getCollege();
		}

		return ReferenceComment.builder()
				.content(dto.getContent())
				.referenceBoard(referenceBoard)
				.user(writer)
				.writerName(writerName)
				.date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.build();
	}


	// 댓글 entity를 ResponseDto로변환
	public CommentResponseDto commentEntityToDto(ReferenceComment entity) {

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
