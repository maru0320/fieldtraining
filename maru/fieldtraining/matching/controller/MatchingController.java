package com.fieldtraining.matching.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.matching.dto.MatchRequestDto;
import com.fieldtraining.matching.dto.MatchRequestViewDto;
import com.fieldtraining.matching.dto.MatchedInfoDto;
import com.fieldtraining.matching.dto.TeacherNameDto;
import com.fieldtraining.matching.service.MatchingService;

import jakarta.persistence.EntityNotFoundException;

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

 // 유저의 매칭 여부 확인
    @GetMapping("/check/{userId}")
    public ResponseEntity<Map<String, Boolean>> checkIfMatched(@PathVariable Long userId) {
        boolean isMatched = matchingService.isUserMatched(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMatched", isMatched);
        return ResponseEntity.ok(response);
    }
    
    // userId로 매칭된 matchId 조회
    @GetMapping("/match-id/{userId}")
    public ResponseEntity<Map<String, Long>> getMatchId(@PathVariable Long userId) {
        try {
            Long matchId = matchingService.getMatchIdByUserId(userId);  // userId로 매칭 ID 조회
            Map<String, Long> response = new HashMap<>();
            response.put("matchId", matchId);
            return ResponseEntity.ok(response);  // matchId 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 매칭되지 않으면 404 반환
        }
    }
    
    // 매칭 ID로 학생과 선생님 정보 조회
    @GetMapping("/matched-info/{matchId}")
    public ResponseEntity<MatchedInfoDto> getMatchedInfo(@PathVariable Long matchId) {
        try {
            MatchedInfoDto matchedInfo = matchingService.getMatchedInfo(matchId);  // matchId로 학생, 선생님 정보 가져옴
            return ResponseEntity.ok(matchedInfo);  // 200 OK와 함께 매칭된 정보 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 매칭된 정보가 없으면 404 반환
        }
    }
    
    // 선생님이 매칭 삭제
    @DeleteMapping("/delete/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long matchId) {
    	try {
    		matchingService.deleteMatch(matchId); // 매칭 삭제 서비스
    		return ResponseEntity.ok("매칭이 삭제되었습니다.");
    	} catch(EntityNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("매칭을 찾을 수 없습니다.");
    	}
    }
    
    // 학생의 매칭 여부 확인
    @GetMapping("/checked/{studentId}")
    public ResponseEntity<Map<String, Boolean>> checkIfMatch(@PathVariable Long studentId) {
        boolean isMatched = matchingService.isStudentMatched(studentId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMatched", isMatched);
        return ResponseEntity.ok(response);
    }
}
