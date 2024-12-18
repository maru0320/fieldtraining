package com.fieldtraining.listmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fieldtraining.data.entity.Professor;
import com.fieldtraining.data.entity.SchoolManager;
import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.ProfessorRepository;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.listmgmt.dto.ProfessorDto;
import com.fieldtraining.listmgmt.dto.StudentDto;
import com.fieldtraining.listmgmt.dto.TeacherDto;
import com.fieldtraining.listmgmt.repository.ListmgmtRepository;
import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.repository.MatchRepository;

@Service
public class ListmgmtService {
	
	private final ListmgmtRepository listmgmtRepository;
	
	private final StudentRepository studentRepository;
	
	private final TeacherRepository teacherRepository;
	
	private final MatchRepository matchRepository;
	
	private final ProfessorRepository professorRepository;
	
	public ListmgmtService(ListmgmtRepository listmgmtRepository, StudentRepository studentRepository,
			TeacherRepository teacherRepository, MatchRepository matchRepository,
			ProfessorRepository professorRepository) {
		this.listmgmtRepository = listmgmtRepository;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
		this.matchRepository = matchRepository;
		this.professorRepository = professorRepository;
	}
	
	public List<User> getAllUsers() {
        return listmgmtRepository.findAll();
    }
	
	// 실습생 목록 가져오기 (StudentDto로 변환)
    public List<StudentDto> getStudents() {
        List<User> users = listmgmtRepository.findStudents();
        return users.stream()
                .filter(user -> user.getStudentDetail() != null)
                .map(user -> {
                    Student student = user.getStudentDetail();
                    return new StudentDto(
                    	student.getId(),
                        student.getName(),
                        student.getCollege(),
                        student.getDepartment(),
                        student.getEmail(),
                        student.getSubject(),
                        student.getPhoneNumber()
                    );
                })
                .collect(Collectors.toList());
    }
    
    // 교사 목록 가져오기 (TeacherDto로 변환)
    public List<TeacherDto> getTeachers() {
        List<User> users = listmgmtRepository.findTeachers();
        return users.stream()
                .filter(user -> user.getTeacherDetail() != null)
                .map(user -> {
                    Teacher teacher = user.getTeacherDetail();
                    return new TeacherDto(
                    		teacher.getId(),
                    		teacher.getName(),
                    		teacher.getEmail(),
                    		teacher.getSchoolName(),
                    		teacher.getSubject()
                    );
                })
                .collect(Collectors.toList());
    }
    
    //학과로 학생 검색
    public List<StudentDto> searchByDepartmentStudent(String department) {
    	List<Student> students = studentRepository.findByDepartment(department);
    	
    	return students.stream()
    			.map(student -> StudentDto.builder()
    			        .name(student.getName())
    			        .college(student.getCollege())
    			        .department(student.getDepartment())
    			        .email(student.getEmail())
    			        .subject(student.getSubject())
    			        .build())
    			    .collect(Collectors.toList());
    }
    
    //교과로 교사 검색
    public List<TeacherDto> searchBySubject(String subject){
    	List<Teacher> teachers = teacherRepository.findBySubject(subject);
    	
    	return teachers.stream()
    			.map(teacher -> TeacherDto.builder()
    					.name(teacher.getName())
    					.email(teacher.getEmail())
    					.schoolName(teacher.getSchoolName())
    					.subject(teacher.getSubject())
    					.build())
    			.collect(Collectors.toList());
    }
    
    //교과로 교수 검색
    public List<ProfessorDto> searchByDepartmentProfessor(String department){
    	List<Professor> professors = professorRepository.findByDepartment(department);
    	
    	return professors.stream()
    			.map(professor -> ProfessorDto.builder()
    					.name(professor.getName())
    					.college(professor.getCollege())
    					.department(professor.getDepartment())
    					.email(professor.getEmail())
    					.phoneNumber(professor.getPhoneNumber())
    					.build())
    			.collect(Collectors.toList());
    }
 
 // 특정 교사에 매칭된 학생 조회
    public List<StudentDto> getStudentsForTeacher(Long teacherId) {
        // 교사 ID로 매칭된 학생 목록 조회
        List<Student> students = matchRepository.findStudentsByTeacherId(teacherId);

        // 학생 정보를 StudentDto로 변환하여 반환
        return students.stream()
            .map(student -> StudentDto.builder()
                .id(student.getId())
                .name(student.getName())
                .college(student.getCollege())
                .department(student.getDepartment())
                .email(student.getEmail())
                .subject(student.getSubject())
                .phoneNumber(student.getPhoneNumber())
                .build())
            .collect(Collectors.toList());
    }
    
    // 특정 사용자의 schoolName으로 교사 목록 가져오기
    public List<TeacherDto> getTeachersByUserSchool(Long userId) {
        // userId로 User 엔티티 조회
        User user = listmgmtRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        // SchoolManager 엔티티 조회 (User -> SchoolManager 관계 설정 필요)
        SchoolManager schoolManager = user.getSchoolManagerDetail(); // SchoolManager 엔티티 가져오기
        if (schoolManager == null) {
            throw new IllegalStateException("학교 관리자가 아닙니다.");
        }

        // 학교 관리자의 schoolName 가져오기
        String schoolName = schoolManager.getSchoolName();
        if (schoolName == null || schoolName.isEmpty()) {
            throw new IllegalStateException("학교 이름 정보가 없습니다.");
        }

        // schoolName으로 교사 목록 필터링
        List<Teacher> teachers = teacherRepository.findBySchoolName(schoolName);

        // Teacher 엔티티를 TeacherDto로 변환하여 반환
        return teachers.stream()
                .map(teacher -> TeacherDto.builder()
                        .id(teacher.getId())
                        .name(teacher.getName())
                        .email(teacher.getEmail())
                        .schoolName(teacher.getSchoolName())
                        .subject(teacher.getSubject())
                        .build())
                .collect(Collectors.toList());
    }


}
