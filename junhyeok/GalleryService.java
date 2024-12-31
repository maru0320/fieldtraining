package com.fieldtraining.gallery.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.ProfessorRepository;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.gallery.dto.GalleryDetailDto;
import com.fieldtraining.gallery.dto.GalleryFileDto;
import com.fieldtraining.gallery.entity.Gallery;
import com.fieldtraining.gallery.entity.GalleryFile;
import com.fieldtraining.gallery.repository.GalleryFileRepository;
import com.fieldtraining.gallery.repository.GalleryRepository;
import com.fieldtraining.managerInfo.repository.CollegeRepository;
import com.fieldtraining.managerInfo.repository.SchoolRepository;

@Service
@Transactional
public class GalleryService {
	
	private static final Logger log = LoggerFactory.getLogger(GalleryService.class);

	private final GalleryRepository galleryRepository;
    private final GalleryFileRepository galleryFileRepository;
    private final UserRepository userRepository;
    
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ProfessorRepository professorRepository;
    private final CollegeRepository collegeRepository;
    private final SchoolRepository schoolRepository;
    
    public GalleryService(GalleryRepository galleryRepository, GalleryFileRepository galleryFileRepository,
    		UserRepository userRepository,
    		StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            ProfessorRepository professorRepository,
            CollegeRepository collegeRepository,
            SchoolRepository schoolRepository) {
        this.galleryRepository = galleryRepository;
        this.galleryFileRepository = galleryFileRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.professorRepository = professorRepository;
        this.collegeRepository = collegeRepository;
        this.schoolRepository = schoolRepository;
    }
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 갤러리 생성
    public void createGallery(String title, String writerID, List<MultipartFile> files) {
        User user = userRepository.findByUserId(writerID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String writerName = null;
        if ("schoolManager".equals(user.getRole())) {
           writerName = user.getSchoolManagerDetail().getSchoolName();
        } else if ("collegeManager".equals(user.getRole())) {
           writerName = user.getCollegeManagerDetail().getCollege();
        } else if ("admin".equals(user.getRole())) {
           writerName = "관리자";
        }

        Gallery gallery = new Gallery();
        gallery.setTitle(title);
        gallery.setWriterName(writerName);
        gallery.setWriter(user);
        gallery.setDate(LocalDate.now());
        galleryRepository.save(gallery);

        // 파일 업로드 처리
        for (MultipartFile file : files) {
            String filePath = saveFile(file);
            GalleryFile galleryFile = new GalleryFile();
            galleryFile.setOriginalName(file.getOriginalFilename());
            galleryFile.setImgUrl(filePath);
            galleryFile.setFileSize(file.getSize());
            galleryFile.setGallery(gallery);
            galleryFileRepository.save(galleryFile);
        }
    }

    // 파일 업로드 처리
    public void uploadFiles(Long boardID, List<MultipartFile> files) {
        Gallery gallery = galleryRepository.findById(boardID)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));

        for (MultipartFile file : files) {
            String filePath = saveFile(file);  // 파일 저장 경로
            log.info("업로드된 파일 경로: {}", filePath);
            GalleryFile galleryFile = GalleryFile.builder()
                    .originalName(file.getOriginalFilename())
                    .imgUrl(filePath)  // 파일 경로 저장
                    .fileSize(file.getSize())
                    .gallery(gallery)
                    .build();

            galleryFileRepository.save(galleryFile);
        }
    }

    private String saveFile(MultipartFile file) {
    	// 경로 구분자를 '/'로 강제 지정
    	String filePath = uploadDir + file.getOriginalFilename();

        try {
            File dest = new File(filePath);
            file.transferTo(dest);
            log.info("파일 저장 성공: {}", filePath);
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 저장 실패");
        }

        // 웹에서 접근할 경로
        String webUrl = "/uploads/" + file.getOriginalFilename();
        log.info("웹 URL 경로: {}", webUrl);
        return webUrl;
    }

    // 갤러리 상세 조회
    public GalleryDetailDto getGalleryDetail(Long boardID) {
        Gallery gallery = galleryRepository.findById(boardID)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));

        List<GalleryFileDto> files = gallery.getFiles().stream()
                .map(file -> GalleryFileDto.builder()
                        .id(file.getId())
                        .originalName(file.getOriginalName())
                        .imgUrl(file.getImgUrl())
                        .fileSize(file.getFileSize())
                        .build())
                .collect(Collectors.toList());

        return GalleryDetailDto.builder()
                .boardID(gallery.getBoardID())
                .title(gallery.getTitle())
                .writerName(gallery.getWriterName())
                .date(gallery.getDate())
                .files(files)
                .build();
    }

    // 갤러리 수정
    public void updateGallery(Long boardID, String title, String writerName, List<MultipartFile> files) {
        Gallery gallery = galleryRepository.findById(boardID)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));

        // 갤러리 제목 수정
        gallery.setTitle(title);
        gallery.setWriterName(writerName);
        galleryRepository.save(gallery);

        // 기존 파일 삭제 (옵션)
        galleryFileRepository.deleteAllByGalleryBoardID(boardID);

        // 새 파일 업로드
        for (MultipartFile file : files) {
            String filePath = saveFile(file);
            GalleryFile galleryFile = new GalleryFile();
            galleryFile.setOriginalName(file.getOriginalFilename());
            galleryFile.setImgUrl(filePath);
            galleryFile.setFileSize(file.getSize());
            galleryFile.setGallery(gallery);
            galleryFileRepository.save(galleryFile);
        }
    }

    // 갤러리 삭제
    public void deleteGallery(Long boardID) {
        Gallery gallery = galleryRepository.findById(boardID)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));

        // 갤러리 파일 삭제
        galleryFileRepository.deleteAllByGalleryBoardID(boardID);

        // 갤러리 삭제
        galleryRepository.delete(gallery);
    }

    // 파일 삭제
    public void deleteFile(Long fileID) {
        GalleryFile galleryFile = galleryFileRepository.findById(fileID)
                .orElseThrow(() -> new RuntimeException("File not found"));

        galleryFileRepository.delete(galleryFile);
    }
    
    // 역할에 따라 소속 정보 가져오기
    public String getAffiliationByRole(Long id, String role) {
        if ("student".equals(role)) {
            return studentRepository.findById(id)
                    .map(student -> student.getSchoolName())
                    .orElse(null);
        } else if ("teacher".equals(role)) {
            return teacherRepository.findById(id)
                    .map(teacher -> teacher.getSchoolName())
                    .orElse(null);
        } else if ("schoolManager".equals(role)) {
            return schoolRepository.findById(id)
                    .map(manager -> manager.getSchoolName())
                    .orElse(null);
        } else if ("professor".equals(role)) {
            return professorRepository.findById(id)
                    .map(professor -> professor.getCollege())
                    .orElse(null);
        } else if ("collegeManager".equals(role)) {
            return collegeRepository.findById(id)
                    .map(manager -> manager.getCollege())
                    .orElse(null);
        }
        return null;
    }


    // 소속 정보에 맞는 갤러리 목록 조회
    public List<GalleryDetailDto> getGalleriesByAffiliation(String affiliation) {
        log.info("Fetching galleries for affiliation: {}", affiliation); // affiliation 값 확인

        List<Gallery> galleries = galleryRepository.findByAffiliation(affiliation);
        log.info("Galleries found: {}", galleries.size()); // 갤러리 개수 확인

        return galleries.stream()
                .map(gallery -> GalleryDetailDto.builder()
                        .boardID(gallery.getBoardID())
                        .title(gallery.getTitle())
                        .writerName(gallery.getWriterName())
                        .date(gallery.getDate())
                        .files(gallery.getFiles().stream()
                                .map(file -> GalleryFileDto.builder()
                                        .id(file.getId())
                                        .originalName(file.getOriginalName())
                                        .imgUrl(file.getImgUrl())
                                        .fileSize(file.getFileSize())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
    
    // GalleryService에 최근 갤러리 가져오는 메서드 추가
    public List<GalleryDetailDto> getRecentGalleries() {
        // 최근 5개의 갤러리 데이터를 가져옴
        List<Gallery> galleries = galleryRepository.findByOrderByDateDesc();

        // Gallery -> GalleryDetailDto 변환 후 반환
        return galleries.stream()
                .map(gallery -> GalleryDetailDto.builder()
                        .boardID(gallery.getBoardID())
                        .title(gallery.getTitle())
                        .writerName(gallery.getWriterName())
                        .date(gallery.getDate())
                        .files(gallery.getFiles().stream()
                                .map(file -> GalleryFileDto.builder()
                                        .id(file.getId())
                                        .originalName(file.getOriginalName())
                                        .imgUrl(file.getImgUrl())
                                        .fileSize(file.getFileSize())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    // 페이징된 갤러리 데이터를 가져오는 메서드
    public Page<GalleryDetailDto> getPagedGalleries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Gallery> galleryPage = galleryRepository.findAllByOrderByDateDesc(pageable);

        return galleryPage.map(gallery -> GalleryDetailDto.builder()
                .boardID(gallery.getBoardID())
                .title(gallery.getTitle())
                .writerName(gallery.getWriterName())
                .date(gallery.getDate())
                .files(gallery.getFiles().stream()
                        .map(file -> GalleryFileDto.builder()
                                .id(file.getId())
                                .originalName(file.getOriginalName())
                                .imgUrl(file.getImgUrl())
                                .fileSize(file.getFileSize())
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

}
