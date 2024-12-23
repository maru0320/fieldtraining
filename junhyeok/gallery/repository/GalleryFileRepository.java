package com.fieldtraining.gallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.gallery.entity.GalleryFile;

public interface GalleryFileRepository extends JpaRepository<GalleryFile, Long>{
	// 특정 Gallery의 boardID를 기준으로 모든 파일을 삭제
    void deleteAllByGalleryBoardID(Long boardID);
    
    // 특정 GalleryFile ID를 기준으로 파일 삭제 (기본적인 삭제)
    void deleteById(Long fileID);
    
    // Gallery와 연결된 파일들을 조회
    List<GalleryFile> findByGalleryBoardID(Long boardID);
}
