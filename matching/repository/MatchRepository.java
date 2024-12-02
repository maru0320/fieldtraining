package com.packt.fieldtraining.matching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.packt.fieldtraining.matching.entity.Match;
import com.packt.fieldtraining.matching.entity.MatchStatus;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // 선생님이 승인 대기 중인 매칭 목록 조회
    List<Match> findByTeacherIdAndStatus(Long teacherId, MatchStatus status);

    Match findByStudentId(Long studentId);

    void deleteById(Long id);
    
    @Query("SELECT m FROM Match m JOIN FETCH m.student JOIN FETCH m.teacher WHERE m.teacher.id = :teacherId AND m.matchApproved = false")
    List<Match> findByTeacherIdAndMatchApprovedFalse(@Param("teacherId") Long teacherId);

    // 학생 ID와 매칭 상태로 매칭을 조회
    Match findByStudentIdAndMatchApproved(Long studentId, boolean matchApproved);

    // 학생 ID와 매칭 상태로 매칭을 조회
    List<Match> findByStudentIdAndMatchApproved(Long studentId, Boolean matchApproved);
    
    // student 엔티티의 ID를 기준으로 매칭 승인 여부를 체크
    List<Match> findByStudent_IdAndMatchApproved(Long studentId, Boolean matchApproved);
}
