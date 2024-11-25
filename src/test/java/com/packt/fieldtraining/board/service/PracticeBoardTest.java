package com.packt.fieldtraining.board.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.packt.fieldtraining.board.dto.PracticeBoardRequestDto;
import com.packt.fieldtraining.board.dto.PracticeBoardResponseDto;
import com.packt.fieldtraining.board.entity.PracticeBoard;
import com.packt.fieldtraining.board.repository.PracticeBoardRepository;
import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.common.entity.Teacher;
import com.packt.fieldtraining.common.entity.User;
import com.packt.fieldtraining.common.repository.StudentRepository;
import com.packt.fieldtraining.common.repository.TeacherRepository;
import com.packt.fieldtraining.common.repository.UserRepository;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
@TestPropertySource(locations = "classpath:application.properties")
public class PracticeBoardTest {

    @Autowired
    private PracticeBoardService practiceBoardService;

    @Autowired
    private PracticeBoardRepository practiceBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @Rollback(false) // 롤백을 원치 않으면 false, 테스트가 끝나면 커밋
    public void testCreate() {
        try {
            // Given: User(Student) 생성
            User user = new User();
            user.setUserId("jiji12");
            user.setApproval(true);
            user.setPassword("jijiji12");
            user.setRole("STUDENT");
            userRepository.save(user);

            Student student = new Student();
            student.setName("최지원");
            student.setCollege("배재대학교");
            student.setDepartment("컴퓨터공학과");
            student.setEmail("qwer122@naver.com");
            student.setPhoneNumber("01045678151");
            student.setSchoolName("도마초");
            student.setStudentNumber(1234567);
            student.setSubject("수학");
            student.setUser(user);
            user.setStudentDetail(student);
            studentRepository.save(student);

            // Given: 게시글 DTO 생성
            PracticeBoardRequestDto boardDto = PracticeBoardRequestDto.builder()
                    .writerID(user.getId())
                    .title("테스트 게시글")
                    .fileName("testfile.txt")
                    .content("테스트 내용입니다.")
                    .build();
            
            PracticeBoardRequestDto boardTest = PracticeBoardRequestDto.builder()
                    .writerID(user.getId())
                    .title("테스트 게시글2")
                    .fileName("testfile2.txt")
                    .content("테스트2 내용임")
                    .build();
            
            PracticeBoardRequestDto boardTest3 = PracticeBoardRequestDto.builder()
                    .writerID(user.getId())
                    .title("테스트 게시글3")
                    .fileName("testfile3.txt")
                    .content("테스트3 내용임")
                    .build();

            // When: 게시글 생성
            PracticeBoardResponseDto response = practiceBoardService.createBoard(boardDto);
            PracticeBoardResponseDto response2 = practiceBoardService.createBoard(boardTest);
            PracticeBoardResponseDto response3 = practiceBoardService.createBoard(boardTest3);

            // Then: 결과 출력
            System.out.println("Created Board: " + response);
            System.out.println("Created Board: " + response2);
            System.out.println("Created Board: " + response3);
            
            List<PracticeBoard> results = practiceBoardRepository.searchByKeyword("내용임");

            results.forEach(board -> {
                System.out.println("Title: " + board.getTitle());
                System.out.println("Content: " + board.getContent());
                System.out.println("Writer: " + board.getWriterName());
            });

            // 추가 데이터 확인
            List<PracticeBoard> boards = practiceBoardRepository.findAll();
            boards.forEach(board -> System.out.println("Board in DB: " + board));

        } catch (Exception e) {
            // 예외 발생 시 트랜잭션을 롤백하도록 처리
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e; // 예외를 다시 던져서 테스트 실패로 처리
        }
    }

    @Test
    @Rollback(false) // 롤백을 원치 않으면 false, 테스트가 끝나면 커밋
    public void testCreate2() {
        try {
            // Given: User(teacher) 생성
            User user = new User();
            user.setUserId("qwer123");
            user.setApproval(true);
            user.setPassword("qwer123");
            user.setRole("TEACHER");
            userRepository.save(user);

            Teacher teacher = new Teacher();
            teacher.setName("박준");
            teacher.setEmail("park21@naver.com");
            teacher.setPhoneNumber("01045679841");
            teacher.setSchoolName("도마중");
            teacher.setOfficeNumber("01085884564");
            teacher.setSubject("과학");
            teacher.setUser(user);
            user.setTeacherDetail(teacher);
            teacherRepository.save(teacher);

            // Given: 게시글 DTO 생성
            PracticeBoardRequestDto boardDto = PracticeBoardRequestDto.builder()
                    .writerID(user.getId())
                    .title("일기장입니다")
                    .fileName("fileupload.txt")
                    .content("본문은 무엇을 넣어야 할까요")
                    .build();

            // When: 게시글 생성
            PracticeBoardResponseDto response = practiceBoardService.createBoard(boardDto);

            // Then: 결과 출력
            System.out.println("Created Board: " + response);

            // 추가 데이터 확인
            List<PracticeBoard> boards = practiceBoardRepository.findAll();
            boards.forEach(board -> System.out.println("Board in DB: " + board));

        } catch (Exception e) {
            // 예외 발생 시 트랜잭션을 롤백하도록 처리
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e; // 예외를 다시 던져서 테스트 실패로 처리
        }
    }
}
