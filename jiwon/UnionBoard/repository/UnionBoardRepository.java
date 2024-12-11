package com.fieldtraining.UnionBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.PracticeBoard.entity.PracticeBoard;
import com.fieldtraining.UnionBoard.entity.UnionBoard;


public interface UnionBoardRepository extends JpaRepository<UnionBoard, Long> {

	// 학생: 자신이 쓴 글 + 매칭된 선생님의 글
	@Query("SELECT b FROM UnionBoard b WHERE b.writer.id = :userId " +
			"OR b.writer.id IN (SELECT m.teacher.id FROM Match m WHERE m.student.id = :userId)")
	Page<UnionBoard> findBoardsForStudent(Long userId, Pageable pageable);

	@Query("SELECT b FROM UnionBoard b WHERE (b.writer.id = :userId " +
			"OR b.writer.id IN (SELECT m.teacher.id FROM Match m WHERE m.student.id = :userId)) " +
			"AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%)")
	Page<UnionBoard> findBoardsForStudentWithKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


	// 선생님: 자신이 쓴 글 + 매칭된 학생의 글
	@Query("SELECT b FROM UnionBoard b WHERE b.writer.id = :userId " +
			"OR b.writer.id IN (SELECT m.student.id FROM Match m WHERE m.teacher.id = :userId)")
	Page<UnionBoard> findBoardsForTeacher(Long userId, Pageable pageable);

	@Query("SELECT b FROM UnionBoard b WHERE (b.writer.id = :userId " +
			"OR b.writer.id IN (SELECT m.student.id FROM Match m WHERE m.teacher.id = :userId)) " +
			"AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%)")
	Page<UnionBoard> findBoardsForTeacherWithKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


	// 교수: 속한 학교/학과의 학생이 쓴 글
	@Query("SELECT b FROM UnionBoard b WHERE b.writer.id IN " +
			"(SELECT s.id FROM Student s WHERE s.college = :college AND s.department = :department)")
	Page<UnionBoard> findBoardsForProfessor(String college, String department, Pageable pageable);

	@Query("SELECT b FROM UnionBoard b WHERE b.writer.id IN " +
			"(SELECT s.id FROM Student s WHERE s.college = :college AND s.department = :department) " +
			"AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%)")
	Page<UnionBoard> findBoardsForProfessorWithKeyword(@Param("college") String college, @Param("department") String department, @Param("keyword") String keyword, Pageable pageable);

	// 전체관리자: 그냥 모든 글 
	@Query("SELECT b FROM UnionBoard b")
	Page<UnionBoard> findBoardsForAdmin(Pageable pageable);

	@Query("SELECT b FROM UnionBoard b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
	Page<UnionBoard> findBoardsForAdminWithKeyword(@Param("keyword") String keyword, Pageable pageable);


}

