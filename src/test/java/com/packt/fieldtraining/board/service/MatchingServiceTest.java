package com.packt.fieldtraining.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.common.entity.Teacher;
import com.packt.fieldtraining.common.repository.StudentRepository;
import com.packt.fieldtraining.common.repository.TeacherRepository;
import com.packt.fieldtraining.matching.dto.MatchRequestDto;
import com.packt.fieldtraining.matching.dto.MatchResponseDto;
import com.packt.fieldtraining.matching.repository.MatchRepository;
import com.packt.fieldtraining.matching.service.MatchingService;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
@TestPropertySource(locations="classpath:application.properties")
public class MatchingServiceTest {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Test
	@Rollback(false)
    public void testRequestMatch() {
        // 데이터 준비
        Student student = studentRepository.save(Student.builder()
                .name("John Doe")
                .college("Engineering")
                .department("Computer Science")
                .studentNumber(12345)
                .subject("Java Programming")
                .email("john.doe@example.com")
                .phoneNumber("010-1234-5678")
                .schoolName("Spring University")
                .build());

        Teacher teacher = teacherRepository.save(Teacher.builder()
                .name("Jane Smith")
                .subject("Java Programming")
                .email("jane.smith@example.com")
                .phoneNumber("010-9876-5432")
                .schoolName("Spring University")
                .officeNumber("101")
                .build());

        // 매칭 요청
        MatchRequestDto requestDto = new MatchRequestDto(student.getId(), teacher.getId());
        MatchResponseDto responseDto = matchingService.requestMatch(requestDto);

        // 결과 검증
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getStudentName()).isEqualTo("John Doe");
        assertThat(responseDto.getTeacherName()).isEqualTo("Jane Smith");
        assertThat(responseDto.isApproved()).isFalse();
    }

    @Test
	@Rollback(false)
    public void testApproveMatch() {
        // 매칭 생성
        Student student = studentRepository.save(Student.builder()
                .name("John Doe")
                .college("Engineering")
                .department("Computer Science")
                .studentNumber(12345)
                .subject("Java Programming")
                .email("john.doe@example.com")
                .phoneNumber("010-1234-5678")
                .schoolName("Spring University")
                .build());

        Teacher teacher = teacherRepository.save(Teacher.builder()
                .name("Jane Smith")
                .subject("Java Programming")
                .email("jane.smith@example.com")
                .phoneNumber("010-9876-5432")
                .schoolName("Spring University")
                .officeNumber("101")
                .build());

        MatchRequestDto requestDto = new MatchRequestDto(student.getId(), teacher.getId());
        MatchResponseDto responseDto = matchingService.requestMatch(requestDto);

        // 매칭 승인
        MatchResponseDto approvedResponse = matchingService.approveMatch(responseDto.getMatchId());

        // 결과 검증
        assertThat(approvedResponse).isNotNull();
        assertThat(approvedResponse.isApproved()).isTrue();
    }
}
