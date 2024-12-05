package com.fieldtraining.PracticeBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.PracticeBoard.entity.PracticeBoard;
import com.fieldtraining.data.entity.User;


public interface PracticeBoardRepository extends JpaRepository<PracticeBoard, Long> {

    // 키워드 검색
    @Query("SELECT pb FROM PracticeBoard pb " +
            "WHERE pb.title LIKE CONCAT('%', :keyword, '%')" + 
            "OR pb.content LIKE CONCAT('%', :keyword, '%')" +
            "OR pb.writerName LIKE CONCAT('%', :keyword, '%')")
    List<PracticeBoard> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT pb FROM PracticeBoard pb " +
            "WHERE pb.writer = :user " +
            "OR pb.writer IN (SELECT m.teacher.user FROM Match m WHERE m.student.user = :user AND m.matchApproved = true) " +
            "OR pb.writer IN (SELECT m.student.user FROM Match m WHERE m.teacher.user = :user AND m.matchApproved = true) " +
            "OR pb.writer IN (SELECT p.user FROM Professor p WHERE p.college = :college AND p.department = :department)")
     List<PracticeBoard> listRelevantBoards(
             @Param("user") User user,
             @Param("college") String college,
             @Param("department") String department);
}

