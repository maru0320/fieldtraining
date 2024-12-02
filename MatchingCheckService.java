package com.packt.fieldtraining.matchingCheck.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.common.repository.StudentRepository;
import com.packt.fieldtraining.common.repository.TeacherRepository;
import com.packt.fieldtraining.match.entity.Match;
import com.packt.fieldtraining.match.repository.MatchRepository;
import com.packt.fieldtraining.matchingCheck.dto.MatchingDto;

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
		        throw new RuntimeException("No matches found in the database.");
		 }
		 
		 return matches.stream()
		            .map(match -> {
		                if (match.getStudent() == null || match.getTeacher() == null) {
		                    throw new RuntimeException("Match contains null references for student or teacher.");
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
