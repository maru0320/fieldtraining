package com.fieldtraining.matching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.matching.dto.MatchRequestDto;
import com.fieldtraining.matching.dto.MatchRequestViewDto;
import com.fieldtraining.matching.dto.MatchedInfoDto;
import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.entity.MatchStatus;
import com.fieldtraining.matching.repository.MatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MatchingService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private UserRepository userRepository;

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

	// 학생의 매칭 여부를 확인하는 메서드
	public boolean isUserMatched(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
		
		boolean apploved = false;
		if ("student".equals(user.getRole())) {
			apploved = matchRepository.existsByStudentIdAndMatchApproved(userId, true);
	      } else if ("teacher".equals(user.getRole())) {
	    	  apploved = matchRepository.existsByTeacherIdAndMatchApproved(userId, true);
	      }
		// 매칭된 데이터가 있는지 확인
		return apploved;
		
	}
	
	 public Long getMatchIdByUserId(Long userId) {
	        // studentId로 매칭된 데이터를 찾을 경우
	        Match matchByStudent = matchRepository.findByStudentId(userId);
	        if (matchByStudent != null) {
	            return matchByStudent.getId();  // 해당 matchId 반환
	        }

	        // teacherId로 매칭된 데이터를 찾을 경우
	        Match matchByTeacher = matchRepository.findByTeacherId(userId);
	        if (matchByTeacher != null) {
	            return matchByTeacher.getId();  // 해당 matchId 반환
	        }

	        throw new EntityNotFoundException("매칭된 데이터가 없습니다. userId: " + userId);
	    }
	
	public MatchedInfoDto getMatchedInfo(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("매칭된 데이터가 없습니다. matchId: " + matchId));

        // 학생 정보
        Student student = studentRepository.findById(match.getStudent().getId())
                .orElseThrow(() -> new EntityNotFoundException("학생 정보가 없습니다. studentId: " + match.getStudent().getId()));

        // 선생님 정보
        Teacher teacher = teacherRepository.findById(match.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("선생님 정보가 없습니다. teacherId: " + match.getTeacher().getId()));

        // DTO로 반환
        return MatchedInfoDto.builder()
        		.id(match.getId())
                .studentName(student.getName())
                .studentSchool(student.getSchoolName())
                .studentSubject(student.getSubject())
                .studentEmail(student.getEmail())
                .studentCollege(student.getCollege())
                .studentDepartment(student.getDepartment())
                .studentPhoneNumber(student.getPhoneNumber())
                .teacherName(teacher.getName())
                .teacherSchool(teacher.getSchoolName())
                .teacherSubject(teacher.getSubject())
                .teacherEmail(teacher.getEmail())
                .teacherPhoneNumber(teacher.getPhoneNumber())
                .teacherOfficeNumber(teacher.getOfficeNumber())
                .build();
    }
	
	// 매칭 삭제
	public void deleteMatch(Long matchId) {
		Match match = matchRepository.findById(matchId).orElseThrow(() -> new EntityNotFoundException("매칭을 찾을 수 없습니다. matchId: " + matchId));
        matchRepository.delete(match);  // 매칭 삭제
	}
	
    // 학생의 매칭 여부를 확인하는 메서드
    public boolean isStudentMatched(Long studentId) {
        // 매칭된 데이터가 있는지 확인
        return matchRepository.existsByStudentIdAndMatchApproved(studentId, true);
    }
    
}
