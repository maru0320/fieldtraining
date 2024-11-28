package com.packt.fieldtraining.matching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.data.entity.Student;
import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.data.repository.StudentRepository;
import com.packt.fieldtraining.data.repository.TeacherRepository;
import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchRequestViewDto;
import com.packt.fieldtraining.matching.entity.Match;
import com.packt.fieldtraining.matching.repository.MatchRepository;

@Service
public class MatchingService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private StudentRepository studentRepository;  // 학생 리포지토리
    
    @Autowired
    private TeacherRepository teacherRepository;  // 선생님 리포지토리

    
    // 선생님 목록 조회
    public List<Teacher> getTeachersBySubject(Long studentId) {
    	// 학생 정보를 가져오기
    	Student student = studentRepository.findById(studentId)
    			.orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));
    	
    	// 학생의 subject를 가져옵니다.
    	String studentSubject = student.getSubject();
    	
    	// 학생 subject와 동일한 선생님만 반환
        return teacherRepository.findBySubject(studentSubject); // TeacherRepository에서 선생님 목록을 가져옵니다.
    }


    // 매칭 신청
    public void applyMatch(MatchRequestDto matchRequestDto) {
        // 학생과 선생님의 정보 가져오기
        Student student = studentRepository.findById(matchRequestDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));
        Teacher teacher = teacherRepository.findById(matchRequestDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("선생님을 찾을 수 없습니다."));

        // 매칭 엔티티 생성
        Match match = new Match();
        match.setStudent(student);
        match.setTeacher(teacher);
        match.setMatchApproved(false);  // 대기 중
        matchRepository.save(match);
    }

    // 매칭 승인/거절 처리
    @Transactional
    public void approveMatch(Long matchId, boolean approve) {
        // 매칭 엔티티 가져오기
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        if (approve) {
            // 승인 처리
            match.setMatchApproved(true);  // 매칭 승인
            matchRepository.save(match);   // 매칭 상태 업데이트
            System.out.println("매칭이 승인되었습니다: " + matchId);
        } else {
            // 거절 처리 (매칭 삭제)
            matchRepository.delete(match);  // 매칭 삭제
            System.out.println("매칭이 거절되어 삭제되었습니다: " + matchId);
        }
    }



    
    public void rejectMatch(Long matchId) {
    	matchRepository.deleteById(matchId);
    }

    // 선생님이 승인 대기 중인 매칭 목록을 조회
    public List<MatchRequestViewDto> getPendingMatchesByTeacherId(Long teacherId) {
        // 선생님 ID로 승인되지 않은 매칭 목록 조회
        List<Match> matches = matchRepository.findByTeacherIdAndMatchApprovedFalse(teacherId);

        // DTO로 변환하여 반환
        return matches.stream().map(match -> {
            if (match.getStudent() != null) {
                return new MatchRequestViewDto(
                        match.getId(),
                        match.getStudent().getName(),  // 학생 이름
                        String.valueOf(match.getStudent().getStudentNumber()),  // 학생 학번
                        match.isMatchApproved()
                );
            } else {
                throw new RuntimeException("학생 정보가 존재하지 않습니다.");
            }
        }).collect(Collectors.toList());
    }


    // 학생의 매칭 상태 조회
    public MatchRequestViewDto getMatchStatusByStudentId(Long studentId) {
        Match match = matchRepository.findByStudentIdAndMatchApproved(studentId, false);
        if (match == null) {
            throw new RuntimeException("매칭을 찾을 수 없습니다.");
        }

        if (match.getStudent() != null) {
            return new MatchRequestViewDto(
            		match.getId(),
                    match.getStudent().getName(),  // 학생 이름
                    String.valueOf(match.getStudent().getStudentNumber()),  // 학생 학번
                    match.isMatchApproved()
            );
        } else {
            throw new RuntimeException("학생 정보가 존재하지 않습니다.");
        }
    }
}
