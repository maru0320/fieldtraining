package com.packt.fieldtraining.matching.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchRequestViewDto;
import com.packt.fieldtraining.matching.dto.TeacherNameDto;
import com.packt.fieldtraining.matching.service.MatchingService;

@CrossOrigin(origins = "http://localhost:3000") // 리액트 앱의 URL을 지정
@RestController
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // 선생님 목록 조회
    @GetMapping("/teachers")
    public List<TeacherNameDto> getTeachers(@RequestParam("studentId") Long studentId) {
    	// 서비스에서 학생의 subject에 맞는 선생님 목록을 조회
    	List<Teacher> teachers = matchingService.getTeachersBySubject(studentId);
    	
    	// TeacherNameDto로 변환하여 반환
        return teachers.stream()
        		.map(teacher -> new TeacherNameDto(teacher.getId(), teacher.getName()))
        		.collect(Collectors.toList());
    }

    // 매칭 신청 API
    @PostMapping("/apply")
    public String applyMatch(@RequestBody MatchRequestDto matchRequestDto) {
        matchingService.applyMatch(matchRequestDto);
        return "매칭 신청이 완료되었습니다.";
    }

    // 매칭 승인/거절 처리
    @PostMapping("/approve/{matchId}")
    public String approveMatch(@PathVariable Long matchId, @RequestParam boolean approve) {
        if (matchId == null) {
            throw new IllegalArgumentException("유효한 매칭 ID가 아닙니다.");
        }
        matchingService.approveMatch(matchId, approve); // 서비스 호출
        return approve ? "매칭이 승인되었습니다." : "매칭이 거절되어 삭제되었습니다.";
    }


    
    @PostMapping("/reject/{matchId}")
    public String rejectMatch(@PathVariable Long matchId) {
    	// 매칭 거절 시 매칭 삭제
    	matchingService.rejectMatch(matchId);
    	return "매칭이 거절되어 삭제되었습니다.";
    }

 // 매칭 승인 대기 목록 조회 API
    @GetMapping("/pending/{teacherId}")
    public List<MatchRequestViewDto> getPendingMatches(@PathVariable Long teacherId) {
        // 서비스에서 대기 중인 매칭 목록을 가져옵니다.
        return matchingService.getPendingMatchesByTeacherId(teacherId);
    }

    // 학생의 매칭 상태 조회
    @GetMapping("/status/{studentId}")
    public MatchRequestViewDto getMatchStatus(@PathVariable Long studentId) {
        return matchingService.getMatchStatusByStudentId(studentId);
    }
}
