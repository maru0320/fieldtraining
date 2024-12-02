package com.fieldtraining.matching.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.matching.dto.MatchRequestDto;
import com.fieldtraining.matching.dto.MatchRequestViewDto;
import com.fieldtraining.matching.dto.TeacherNameDto;
import com.fieldtraining.matching.service.MatchingService;

@RestController
@RequestMapping("/matching")
@CrossOrigin(origins = "http://localhost:3000")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // 선생님이 승인 대기 중인 매칭 목록 조회
    @GetMapping("/teacher/{teacherId}/pending")
    public ResponseEntity<List<MatchRequestViewDto>> getPendingMatches(@PathVariable Long teacherId) {
        try {
            List<MatchRequestViewDto> pendingMatches = matchingService.getPendingMatchesByTeacherId(teacherId);
            return ResponseEntity.ok(pendingMatches);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 학생 ID로 선생님 목록 조회
    @GetMapping("/teachers/{studentId}")
    public ResponseEntity<List<TeacherNameDto>> getTeachersBySubject(@PathVariable Long studentId) {
        try {
            List<Teacher> teachers = matchingService.getTeachersBySubject(studentId); // 서비스 호출
            List<TeacherNameDto> teacherDtos = teachers.stream()
                    .map(teacher -> new TeacherNameDto(teacher.getId(), teacher.getName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teacherDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 매칭 신청
    @PostMapping("/apply")
    public ResponseEntity<String> applyMatch(@RequestBody MatchRequestDto matchRequestDto) {
        try {
            matchingService.applyMatch(matchRequestDto);
            return ResponseEntity.ok("매칭 신청이 완료되었습니다.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매칭 신청에 실패했습니다.");
        }
    }
    
    

    @PostMapping("/approve/{matchId}")
    public ResponseEntity<String> approveMatch(@PathVariable Long matchId) {
        System.out.println("매칭 승인 호출됨: matchId = " + matchId);
        try {
            matchingService.approveMatch(matchId, true); // 승인 처리
            return ResponseEntity.ok("매칭이 승인되었습니다.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매칭 승인에 실패했습니다.");
        }
    }

 // 매칭 거절 처리 (삭제)
    @PostMapping("/reject/{matchId}")
    public ResponseEntity<String> rejectMatch(@PathVariable Long matchId) {
        try {
            matchingService.rejectMatch(matchId); // 삭제 처리
            return ResponseEntity.ok("매칭이 거절되어 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("매칭 거절에 실패했습니다.");
        }
    }
    
    // 학생 ID로 매칭 상태 조회
    @GetMapping("/status/{studentId}")
    public String getMatchStatus(@PathVariable Long studentId) {
        return matchingService.getMatchStatus(studentId);
    }

}