package com.packt.fieldtraining.Listmgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.packt.fieldtraining.Listmgmt.dto.StudentDto;
import com.packt.fieldtraining.Listmgmt.dto.TeacherDto;
import com.packt.fieldtraining.Listmgmt.repository.ListmgmtRepository;
import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.common.entity.Teacher;
import com.packt.fieldtraining.common.entity.User;
import com.packt.fieldtraining.common.repository.StudentRepository;

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
