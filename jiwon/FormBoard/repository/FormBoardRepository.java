package com.fieldtraining.FormBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.FormBoard.entity.FormBoard;


public interface FormBoardRepository extends JpaRepository<FormBoard, Long> {
	
	// 전체관리자: 그냥 모든 글 
	@Query("SELECT b FROM FormBoard b")
	Page<FormBoard> findBoardforManager(Pageable pageable);

	@Query("SELECT b FROM FormBoard b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
	Page<FormBoard> findBoardforManagerWithKeyword(@Param("keyword") String keyword, Pageable pageable);

}

