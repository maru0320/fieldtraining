package com.fieldtraining.PracticeBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.PracticeBoard.entity.PracticeBoard;


public interface PracticeBoardRepository extends JpaRepository<PracticeBoard, Long> {
	@Query("SELECT pb FROM PracticeBoard pb " +
			"WHERE pb.title LIKE CONCAT('%', :keyword, '%')" + 
			"OR pb.content LIKE CONCAT('%', :keyword, '%')" +
			"OR pb.writerName LIKE CONCAT('%', :keyword, '%')")
	List<PracticeBoard> searchByKeyword(@Param("keyword") String keyword);
	
}
