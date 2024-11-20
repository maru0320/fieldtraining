package com.packt.fieldtraining.repository;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.data.entity.Student;
import com.packt.fieldtraining.data.entity.User;
import com.packt.fieldtraining.data.repository.StudentRepository;
import com.packt.fieldtraining.data.repository.UserRepository;


@TestPropertySource("classpath:application-test.properties")
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class RepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    
    @Test
    @Rollback(false) // 롤백 활성화
    void shouldSaveUserAndStudentSuccessfully() {
        // Given
        User user = User.builder()
            .userId("kim123")
            .password("1234")
            .role("student")
            .isApproval(false)
            .build();
        
        String role1 = user.getRole();
        

        if(role1.equalsIgnoreCase("student")) {
			user.setRoles(Collections.singletonList("ROLE_STUDENT"));
    	  userRepository.save(user);

        Student student = Student.builder()
            .user(user)
            .name("kimmaru")
            .college("배재대")
            .department("컴퓨터공학과")
            .studentNumber(1961005)
            .subject("수학")
            .email("ur4m0804@naver.com")
            .phoneNumber("01040825268")
            .schoolName("영선고")
            .build();

         
        studentRepository.save(student);
        }

        System.out.println(user.getId());
        System.out.println(user.getUserId());
        System.out.println(user.getPassword());
        System.out.println(user.isApproval());
        
//        System.out.println(student.getName());
//        // Then
//        assertNotNull(user.getId());
//        assertNotNull(student.getId());
//        assertEquals("kim123", user.getUserId());
//        assertEquals("kimmaru", student.getName());
    }
}
