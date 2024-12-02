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

import com.packt.fieldtraining.data.entity.CollegeManager;
import com.packt.fieldtraining.data.entity.Professor;
import com.packt.fieldtraining.data.entity.User;
import com.packt.fieldtraining.data.repository.CollegeManagerRepository;
import com.packt.fieldtraining.data.repository.ProfessorRepository;
import com.packt.fieldtraining.data.repository.UserRepository;


@TestPropertySource("classpath:application-test.properties")
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class RepositoryTest {

    @Autowired
    private CollegeManagerRepository collegeManagerRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UserRepository userRepository;

    
    @Test
    @Rollback(false) // 롤백 활성화
    void shouldSaveUserAndStudentSuccessfully() {
        // Given
        User user = User.builder()
            .userId("professor111")
            .password("1234")
            .role("professor")
            .isApproval(false)
            .build();
        
        String role1 = user.getRole();
        

        if(role1.equalsIgnoreCase("collegeManager")) {
			user.setRoles(Collections.singletonList("ROLE_COLLEGE_MANAGER"));
    	  userRepository.save(user);
    	  
   

    	  CollegeManager collegeManager = CollegeManager.builder()                       
					.user(user)
					.address("서울")
					.officeNumber("123123")
					.proofData("데이터1")

					.build();
         
        collegeManagerRepository.save(collegeManager);
        } else if(role1.equalsIgnoreCase("professor")) {
			user.setRoles(Collections.singletonList("ROLE_PROFESSOR"));
	    	  userRepository.save(user);

	    	  Professor professor = Professor.builder()                       
						.user(user)
						.officeNumber("123123")
						.college("배재대")
						.department("컴퓨터공학과")
						.email("ppp@naver.com")
						.name("김교수")
						.phoneNumber("123123")
						.build();
	         
	        professorRepository.save(professor);
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
