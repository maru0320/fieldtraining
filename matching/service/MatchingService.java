package com.packt.fieldtraining.matching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.data.entity.Student;
import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.data.repository.StudentRepository;
import com.packt.fieldtraining.data.repository.TeacherRepository;
import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchRequestViewDto;
import com.packt.fieldtraining.matching.entity.Match;
import com.packt.fieldtraining.matching.entity.MatchStatus;
import com.packt.fieldtraining.matching.repository.MatchRepository;

@Service
public class MatchingService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // 학생의 subject에 맞는 선생님 목록을 반환하는 메서드
    public List<Teacher> getTeachersBySubject(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));

        String studentSubject = student.getSubject(); // 학생의 교과목을 가져옴

        return teacherRepository.findBySubject(studentSubject); // 해당 subject의 선생님 목록을 조회
    }

    // 매칭 신청
    public void applyMatch(MatchRequestDto matchRequestDto) {
        Student student = studentRepository.findById(matchRequestDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));
        Teacher teacher = teacherRepository.findById(matchRequestDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("선생님을 찾을 수 없습니다."));

        // 이미 매칭된 학생인지 확인
        Match existingMatch = matchRepository.findByStudentIdAndMatchApproved(matchRequestDto.getStudentId(), false);
        if (existingMatch != null) {
            throw new RuntimeException("이 학생은 이미 다른 선생님과 매칭되어 있습니다.");
        }

        // 매칭 엔티티 생성
        Match match = new Match();
        match.setStudent(student);
        match.setTeacher(teacher);
        match.setStatus(MatchStatus.PENDING); // 매칭 상태는 "대기 중"
        match.setMatchApproved(false); // 매칭 승인 여부는 "대기 중"

        // 매칭 저장
        matchRepository.save(match);
    }

 // 매칭 승인 처리
    public void approveMatch(Long matchId, boolean approve) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        if (approve) {
            match.setStatus(MatchStatus.APPROVED);
            match.setMatchApproved(true); // 승인 상태 설정
        } else {
            match.setStatus(MatchStatus.REJECTED);
            match.setMatchApproved(false); // 거절 상태 설정
        }

        matchRepository.save(match); // 변경사항 저장
    }

 // 매칭 거절 처리 (테이블에서 삭제)
    public void rejectMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));
        
        // 매칭 삭제
        matchRepository.delete(match);
    }


    // 특정 선생님의 대기 중인 매칭 목록 반환
    public List<MatchRequestViewDto> getPendingMatchesByTeacherId(Long teacherId) {
        // PENDING 상태의 매칭만 조회
        List<Match> matches = matchRepository.findByTeacherIdAndStatus(teacherId, MatchStatus.PENDING);

        // DTO로 변환하여 반환
        return matches.stream()
                .map(match -> new MatchRequestViewDto(
                        match.getId(),
                        match.getStudent().getName(), // 학생 이름
                        String.valueOf(match.getStudent().getStudentNumber()), // 학생 학번
                        match.isMatchApproved()))
                .collect(Collectors.toList());
    }

    public String getMatchStatus(Long studentId) {
        // 매칭 승인되지 않은 대기 중인 매칭을 조회
        List<Match> pendingMatches = matchRepository.findByStudent_IdAndMatchApproved(studentId, false);
        if (!pendingMatches.isEmpty()) {
            return "매칭 신청 대기 중";
        }

        // 승인된 매칭이 있을 경우
        List<Match> approvedMatches = matchRepository.findByStudent_IdAndMatchApproved(studentId, true);
        if (!approvedMatches.isEmpty()) {
            return "매칭 승인 완료";
        }

        return "매칭 상태 없음";
    }
}
