package com.fieldtraining.ReferenceBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.ReferenceBoard.entity.ReferenceBoard;


public interface ReferenceBoardRepository extends JpaRepository<ReferenceBoard, Long> {
	
	// 전체관리자: 그냥 모든 글 
	@Query("SELECT b FROM ReferenceBoard b")
	Page<ReferenceBoard> findBoardforManager(Pageable pageable);

	@Query("SELECT b FROM ReferenceBoard b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
	Page<ReferenceBoard> findBoardforManagerWithKeyword(@Param("keyword") String keyword, Pageable pageable);

}

