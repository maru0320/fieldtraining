package com.fieldtraining.gallery.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.gallery.dto.GalleryDetailDto;
import com.fieldtraining.gallery.service.GalleryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
	
	private static final Logger log = LoggerFactory.getLogger(GalleryController.class);
	private static final String UPLOAD_DIR = "C:/proofData/";
	
	private final GalleryService galleryService;
	
	public GalleryController(GalleryService galleryService) {
		this.galleryService = galleryService;
	}
	
	@PostMapping("/write")
	public ResponseEntity<String> createGallery(
	        @RequestParam("title") String title,
	        @RequestParam("userId") String userId, // 프론트에서 전달된 userId 받기
	        @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

	    try {
	        // 파일이 있을 경우만 처리
	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                if (file.getSize() > 10 * 1024 * 1024) { // 파일 사이즈 10MB 제한
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                            .body("파일 크기는 10MB 이하이어야 합니다.");
	                }
	            }
	        }

	        // 갤러리 작성 서비스 호출
	        galleryService.createGallery(title, userId, files); // 서비스 호출
	        return ResponseEntity.status(HttpStatus.CREATED).body("갤러리가 성공적으로 작성되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("갤러리 작성 실패: " + e.getMessage());
	    }
	}
	
	//이미지 보기
	@GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // 파일 형식에 맞는 MIME 타입 설정
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 갤러리 상세 조회
    @GetMapping("/{boardID}")
    public ResponseEntity<GalleryDetailDto> getGalleryDetail(@PathVariable Long boardID) {
        GalleryDetailDto galleryDetail = galleryService.getGalleryDetail(boardID);
        return ResponseEntity.status(HttpStatus.OK).body(galleryDetail);
    }

    // 갤러리 수정
    @PutMapping("/{boardID}")
    public ResponseEntity<String> updateGallery(
            @PathVariable Long boardID,
            @RequestParam String title,
            @RequestParam String writerName,
            @RequestParam(required = false) List<MultipartFile> files) {
        try {
            galleryService.updateGallery(boardID, title, writerName, files != null ? files : List.of());
            return ResponseEntity.ok("갤러리가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("갤러리 수정에 실패했습니다.");
        }
    }

    // 갤러리 삭제
    @DeleteMapping("/{boardID}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long boardID) {
        galleryService.deleteGallery(boardID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 파일 삭제
    @DeleteMapping("/files/{fileID}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileID) {
        galleryService.deleteFile(fileID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    // 역할과 소속 정보 가져오기
    @GetMapping("/affiliation/{Id}/{role}")
    public ResponseEntity<String> getAffiliationByRole(@PathVariable Long Id, @PathVariable String role) {
        String affiliation = galleryService.getAffiliationByRole(Id, role);
        if (affiliation != null) {
            return ResponseEntity.ok(affiliation);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/galleries/{affiliation}")
    public ResponseEntity<List<GalleryDetailDto>> getGalleriesByAffiliation(@PathVariable String affiliation) {
        List<GalleryDetailDto> galleries = galleryService.getGalleriesByAffiliation(affiliation);
        return ResponseEntity.ok(galleries);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<GalleryDetailDto>> getRecentGalleries() {
        try {
            List<GalleryDetailDto> recentGalleries = galleryService.getRecentGalleries();
            return ResponseEntity.ok(recentGalleries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
