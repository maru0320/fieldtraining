package com.packt.fieldtraining.service.impl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.common.CommonResponse;
import com.packt.fieldtraining.config.security.JwtTokenProvider;
import com.packt.fieldtraining.data.dto.SignInResultDto;
import com.packt.fieldtraining.data.dto.SignUpResultDto;
import com.packt.fieldtraining.data.entity.Manager;
import com.packt.fieldtraining.data.entity.Professor;
import com.packt.fieldtraining.data.entity.Student;
import com.packt.fieldtraining.data.entity.Teacher;
import com.packt.fieldtraining.data.entity.User;
import com.packt.fieldtraining.data.repository.ManagerRepository;
import com.packt.fieldtraining.data.repository.ProfessorRepository;
import com.packt.fieldtraining.data.repository.StudentRepository;
import com.packt.fieldtraining.data.repository.TeacherRepository;
import com.packt.fieldtraining.data.repository.UserRepository;
import com.packt.fieldtraining.service.SignService;

@Service
public class SignServiceImpl implements SignService{

	private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);
	
	public UserRepository userRepository;
	public StudentRepository studentRepository;
	public TeacherRepository teacherRepository;
	public ProfessorRepository professorRepository;
	public ManagerRepository managerRepository;
	public JwtTokenProvider jwtTokenProvider;
	public PasswordEncoder passwordEncoder;
	
	
	@Autowired
    public SignServiceImpl(UserRepository userRepository,
                           JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           ProfessorRepository professorRepository,
                           ManagerRepository managerRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.professorRepository = professorRepository;
        this.managerRepository = managerRepository;
    }
	
	@Override
	public SignUpResultDto join(String id,
			String userId,
			String name,
			String password, 
			String role,
			String college,
			String department,
			int studentNumber,
			String subject,
			String email,
			String phoneNumber,
			String SchoolName,
			String officeNumber,
			String adrress,
			boolean isApproval) {
		LOGGER.info("[JoinResult] 회원 가입 정보 전달");

		if (userRepository.existsByUserId(userId)) {
	        LOGGER.warn("[join] 이미 사용 중인 userId입니다: {}", userId);
	        throw new IllegalArgumentException("이미 사용 중인 사용자 ID입니다: " + userId);
	    }

		
		
		User user = User.builder()
				.userId(userId)
				.password(passwordEncoder.encode(password))
				.role(role)
				.isApproval(isApproval)
				.build();
		
		userRepository.save(user);
		if (role.equalsIgnoreCase("admin")) {
			user.setRoles(Collections.singletonList("ROLE_ADMIN"));
		} else if(role.equalsIgnoreCase("manager")) {
			user.setRoles(Collections.singletonList("ROLE_MANAGER"));
			Manager manager = Manager.builder()
									 .user(user)
									 .address(adrress)
									 .officeNumber(officeNumber)
									 .build();
			managerRepository.save(manager);
			
		} else if(role.equalsIgnoreCase("student")) {
			user.setRoles(Collections.singletonList("ROLE_STUDENT"));
			Student student = Student.builder()
									 .user(user)
									 .name(name)
									 .college(college)
									 .department(department)
									 .studentNumber(studentNumber)
									 .subject(subject)
									 .email(email)
									 .phoneNumber(phoneNumber)
									 .schoolName(SchoolName)
									 .build();
			studentRepository.save(student);
		} else if(role.equalsIgnoreCase("teacher")) {
			user.setRoles(Collections.singletonList("ROLE_TEACHER"));
			Teacher teacher = Teacher.builder()
									 .user(user)
									 .name(name)
									 .subject(subject)
									 .schoolName(SchoolName)
									 .officeNumber(officeNumber)
									 .build();
			teacherRepository.save(teacher);
		} else if(role.equalsIgnoreCase("professor")) {
			user.setRoles(Collections.singletonList("ROLE_PROFESSOR"));
			Professor professor = Professor.builder()
										   .user(user)
										   .name(name)
										   .college(college)
										   .department(department)
										   .email(email)
										   .phoneNumber(phoneNumber)
										   .officeNumber(officeNumber)
										   .build();
			professorRepository.save(professor);
		}
		
		
		User savedUser = userRepository.save(user);
		SignUpResultDto signUpResultDto = new SignUpResultDto();
		
		// 정상 처리 여부 확인
        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (savedUser.getUserId() != null) {  // getUserId() 확인
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            LOGGER.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }

        return signUpResultDto;
    }
	
	@Override
	public SignInResultDto login(String userId, String password) throws RuntimeException{
		LOGGER.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
		User user = userRepository.getByUserId(userId);
		LOGGER.info("[getSignInResult] ID : {}", userId);
		
		LOGGER.info("[getSignInResult] 패스워드 비교 수행");
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException();
		}
		
		LOGGER.info("[getSignInResult] 패스워드 일치");
		
		LOGGER.info("[getSignInResult] SignInResultDto 객체 생성");
		SignInResultDto signInResultDto = SignInResultDto.builder()
				.token(jwtTokenProvider.createToken(String.valueOf(user.getUserId()),
						user.getRoles()))
				.build();
		LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
		setSuccessResult(signInResultDto);
		
		return signInResultDto;	
	}
	
	private void setSuccessResult(SignUpResultDto result) {
		result.setSuccess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}
	private void setFailResult(SignUpResultDto result) {
		result.setSuccess(false);
		result.setCode(CommonResponse.FAIL.getCode());
		result.setMsg(CommonResponse.FAIL.getMsg());
	}


	
}
