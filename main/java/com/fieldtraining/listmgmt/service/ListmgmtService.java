package com.fieldtraining.listmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.listmgmt.dto.StudentDto;
import com.fieldtraining.listmgmt.dto.TeacherDto;
import com.fieldtraining.listmgmt.repository.ListmgmtRepository;

@Service
public class ListmgmtService {
	
	private final ListmgmtRepository listmgmtRepository;
	
	private final StudentRepository studentRepository;
	
	public ListmgmtService(ListmgmtRepository listmgmtRepository, StudentRepository studentRepository) {
		this.listmgmtRepository = listmgmtRepository;
		this.studentRepository = studentRepository;
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
                        student.getName(),
                        student.getCollege(),
                        student.getDepartment(),
                        student.getEmail(),
                        student.getSubject(),
                        student.getStudentNumber()
                    );
                })
                .collect(Collectors.toList());
    }
    
    //학과로 학생 검색
    public List<StudentDto> searchByDepartment(String department) {
    	List<Student> students = studentRepository.findByDepartment(department);
    	
    	return students.stream()
    			.map(student -> StudentDto.builder()
    			        .name(student.getName())
    			        .college(student.getCollege())
    			        .department(student.getDepartment())
    			        .email(student.getEmail())
    			        .subject(student.getSubject())
    			        .studentNumber(student.getStudentNumber())
    			        .build())
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
                    		teacher.getName(),
                    		teacher.getEmail(),
                    		teacher.getSchoolName(),
                    		teacher.getSubject()
                    );
                })
                .collect(Collectors.toList());
    }
}
