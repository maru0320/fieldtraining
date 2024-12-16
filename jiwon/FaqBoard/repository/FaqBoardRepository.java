package com.fieldtraining.FaqBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.FaqBoard.entity.FaqBoard;


public interface FaqBoardRepository extends JpaRepository<FaqBoard, Long> {
	
	// 전체관리자: 그냥 모든 글 
	@Query("SELECT b FROM FaqBoard b")
	Page<FaqBoard> findBoardforManager(Pageable pageable);

	@Query("SELECT b FROM FaqBoard b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
	Page<FaqBoard> findBoardforManagerWithKeyword(@Param("keyword") String keyword, Pageable pageable);

}

