package com.packt.fieldtraining.matching.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.common.entity.Teacher;
import com.packt.fieldtraining.common.repository.StudentRepository;
import com.packt.fieldtraining.common.repository.TeacherRepository;
import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchResponseDto;
import com.packt.fieldtraining.matching.entity.Match;
import com.packt.fieldtraining.matching.repository.MatchRepository;

@Service
@Transactional
public class MatchingService {

	@Autowired
	private final MatchRepository matchRepository;
	
	@Autowired
	private final StudentRepository studentRepository;
	
	@Autowired
	private final TeacherRepository teacherRepository;

	public MatchingService(MatchRepository matchRepository, StudentRepository studentRepository,
			TeacherRepository teacherRepository) {
		this.matchRepository = matchRepository;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
	}
	
	
	// 매칭 요청 생성
	public MatchResponseDto requestMatch(MatchRequestDto requestDto) {
		Optional<Student> studentOpt = studentRepository.findById(requestDto.getStudentId());
		Optional<Teacher> teacherOpt = teacherRepository.findById(requestDto.getTeacherId());
		
		if(studentOpt.isEmpty() || teacherOpt.isEmpty()) {
			throw new IllegalArgumentException("학생 또는 선생님 정보가 잘못되었습니다.");
		}
		
		Match match = Match.builder()
				.student(studentOpt.get())
				.teacher(teacherOpt.get())
				.approved(false) // 승인 대기 상태
				.build();

	Match savedMatch = matchRepository.save(match);
	
	return new MatchResponseDto(
			savedMatch.getId(),
			savedMatch.getStudent().getName(),
			savedMatch.getTeacher().getName(),
			savedMatch.isApproved()
			);
	}
	
	//매칭 승인
	public MatchResponseDto approveMatch(Long matchId) {
		Match match = matchRepository.findById(matchId)
				.orElseThrow(() -> new IllegalArgumentException("매칭 정보가 존재하지 않습니다."));
		
		match.setApproved(true);
		Match approvedMatch = matchRepository.save(match);
		
		return new MatchResponseDto(
				approvedMatch.getId(),
				approvedMatch.getStudent().getName(),
				approvedMatch.getTeacher().getName(),
				approvedMatch.isApproved()
		);
	}
}
