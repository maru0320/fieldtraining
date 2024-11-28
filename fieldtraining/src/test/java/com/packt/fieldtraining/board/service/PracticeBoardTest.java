package com.packt.fieldtraining.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.data.entity.Student;
import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.data.entity.User;
import com.packt.fieldtraining.data.repository.StudentRepository;
import com.packt.fieldtraining.data.repository.TeacherRepository;
import com.packt.fieldtraining.data.repository.UserRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class PracticeBoardTest {

	 @Autowired
	    private TeacherRepository teacherRepository;

	    @Autowired
	    private StudentRepository studentRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Test
	    @Rollback(false) // 롤백 방지
	    public void insertTestData() {
	        // User 엔티티 삽입
	        User user1 = new User();
	        user1.setUserId("student01");
	        user1.setPassword("password");
	        user1.setRole("STUDENT");
	        user1.setApproval(true);

	        User user2 = new User();
	        user2.setUserId("teacher02");
	        user2.setPassword("password");
	        user2.setRole("TEACHER");
	        user2.setApproval(true);
	        
	        User user3 = new User();
	        user3.setUserId("teacher03");
	        user3.setPassword("password");
	        user3.setRole("TEACHER");
	        user3.setApproval(true);
	        
	        User user4 = new User();
	        user4.setUserId("teacher04");
	        user4.setPassword("password");
	        user4.setRole("TEACHER");
	        user4.setApproval(true);
	        
	        User user5 = new User();
	        user5.setUserId("teacher05");
	        user5.setPassword("password");
	        user5.setRole("TEACHER");
	        user5.setApproval(true);

	        // Student 엔티티 삽입
	        Student student = new Student();
	        student.setName("학생1");
	        student.setCollege("대학교");
	        student.setDepartment("컴퓨터학과");
	        student.setStudentNumber(12345);
	        student.setSubject("수학");
	        student.setEmail("student1@university.com");
	        student.setPhoneNumber("010-1234-5678");
	        student.setSchoolName("대학교");
	        student.setUser(user1);  // User와 연결
	        user1.setStudentDetail(student);
	        studentRepository.save(student);
	        userRepository.save(user1);

	        // Teacher 엔티티 삽입
	        Teacher teacher = new Teacher();
	        teacher.setName("박준");
	        teacher.setSubject("수학");
	        teacher.setEmail("teacher1@school.com");
	        teacher.setPhoneNumber("010-9876-5432");
	        teacher.setOfficeNumber("01012334789");
	        teacher.setSchoolName("학교");
	        teacher.setUser(user2);  // User와 연결
	        user2.setTeacherDetail(teacher);
	        teacherRepository.save(teacher);
	        userRepository.save(user2);
	        
	        // Teacher 엔티티 삽입
	        Teacher teacher2 = new Teacher();
	        teacher2.setName("김마루");
	        teacher2.setSubject("과학");
	        teacher2.setEmail("teacher1@school.com");
	        teacher2.setPhoneNumber("010-9876-5432");
	        teacher2.setOfficeNumber("01012334789");
	        teacher2.setSchoolName("학교");
	        teacher2.setUser(user3);  // User와 연결
	        user3.setTeacherDetail(teacher2);
	        teacherRepository.save(teacher2);
	        userRepository.save(user3);
	        
	        // Teacher 엔티티 삽입
	        Teacher teacher3 = new Teacher();
	        teacher3.setName("최지원");
	        teacher3.setSubject("수학");
	        teacher3.setEmail("teacher1@school.com");
	        teacher3.setPhoneNumber("010-9876-5432");
	        teacher3.setOfficeNumber("01012334789");
	        teacher3.setSchoolName("학교");
	        teacher3.setUser(user4);  // User와 연결
	        user4.setTeacherDetail(teacher3);
	        teacherRepository.save(teacher3);
	        userRepository.save(user4);
	        
	        // Teacher 엔티티 삽입
	        Teacher teacher4 = new Teacher();
	        teacher4.setName("장경수");
	        teacher4.setSubject("국어");
	        teacher4.setEmail("teacher1@school.com");
	        teacher4.setPhoneNumber("010-9876-5432");
	        teacher4.setOfficeNumber("01012334789");
	        teacher4.setSchoolName("학교");
	        teacher4.setUser(user5);  // User와 연결
	        user5.setTeacherDetail(teacher4);
	        teacherRepository.save(teacher4);
	        userRepository.save(user5);

	        System.out.println("데이터 삽입 완료!");
	    }
}
