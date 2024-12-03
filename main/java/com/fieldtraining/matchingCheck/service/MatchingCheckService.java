package com.fieldtraining.matchingCheck.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.match.entity.Match;
import com.fieldtraining.match.repository.MatchRepository;
import com.fieldtraining.matchingCheck.dto.MatchingDto;

@Service
public class MatchingCheckService {
	 private final MatchRepository matchRepository;  // MatchRepository 사용
	 private final StudentRepository studentRepository;
	 private final TeacherRepository teacherRepository;
	 
	// 생성자 주입
	 public MatchingCheckService(MatchRepository matchRepository,
	                        StudentRepository studentRepository,
	                        TeacherRepository teacherRepository) {
	     this.matchRepository = matchRepository;
	     this.studentRepository = studentRepository;
	     this.teacherRepository = teacherRepository;
	 }
	 
	 public List<MatchingDto> getAllMatches(){
		 List<Match> matches = matchRepository.findAll();
		 
		 if (matches.isEmpty()) {
		        throw new RuntimeException("매칭 정보가 존재하지 않습니다.");
		 }
		 
		 return matches.stream()
		            .map(match -> {
		                if (match.getStudent() == null || match.getTeacher() == null) {
		                    throw new RuntimeException("매칭 정보가 존재하지 않습니다.");
		                }

		                return MatchingDto.builder()
		                        .studentName(match.getStudent().getName())
		                        .teacherName(match.getTeacher().getName())
		                        .approved(match.isApproved())
		                        .build();
		            })
		            .collect(Collectors.toList());
	 }
}
