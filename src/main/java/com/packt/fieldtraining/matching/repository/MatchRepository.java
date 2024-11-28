package com.packt.fieldtraining.matching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.packt.fieldtraining.matching.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

	@Query("SELECT m FROM Match m JOIN FETCH m.student JOIN FETCH m.teacher WHERE m.teacher.id = :teacherId AND m.matchApproved = false")
    List<Match> findByTeacherIdAndMatchApprovedFalse(@Param("teacherId") Long teacherId);
	
    Match findByStudentIdAndMatchApproved(Long studentId, boolean matchApproved);
    
    void deleteById(Long id);
}
