package com.fieldtraining.NoticeBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.NoticeBoard.entity.NoticeBoard;


public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
	
	// 전체관리자: 그냥 모든 글 
	@Query("SELECT b FROM NoticeBoard b")
	Page<NoticeBoard> findBoardforManager(Pageable pageable);

	@Query("SELECT b FROM NoticeBoard b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
	Page<NoticeBoard> findBoardforManagerWithKeyword(@Param("keyword") String keyword, Pageable pageable);

}

