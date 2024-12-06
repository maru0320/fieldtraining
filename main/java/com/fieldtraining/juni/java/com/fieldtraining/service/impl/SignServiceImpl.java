package com.fieldtraining.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.common.CommonResponse;
import com.fieldtraining.config.security.JwtTokenProvider;
import com.fieldtraining.data.dto.SignInResultDto;
import com.fieldtraining.data.dto.SignUpResultDto;
import com.fieldtraining.data.entity.CollegeManager;
import com.fieldtraining.data.entity.Professor;
import com.fieldtraining.data.entity.SchoolManager;
import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.CollegeManagerRepository;
import com.fieldtraining.data.repository.ProfessorRepository;
import com.fieldtraining.data.repository.SchoolManagerRepository;
import com.fieldtraining.data.repository.StudentRepository;
import com.fieldtraining.data.repository.TeacherRepository;
import com.fieldtraining.data.repository.UserRepository;
import com.fieldtraining.service.SignService;

@Service
public class SignServiceImpl implements SignService{

	private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);
	
	@Value("${file.upload-dir}")  // 파일 저장 경로
    private String uploadDir;

	public UserRepository userRepository;
	public StudentRepository studentRepository;
	public TeacherRepository teacherRepository;
	public ProfessorRepository professorRepository;
	public CollegeManagerRepository collegeManagerRepository;
	public SchoolManagerRepository schoolManagerRepository;
	public JwtTokenProvider jwtTokenProvider;
	public PasswordEncoder passwordEncoder;
	public CollegeManager collegeManager;



	@Autowired
	public SignServiceImpl(UserRepository userRepository,
			JwtTokenProvider jwtTokenProvider,
			PasswordEncoder passwordEncoder,
			StudentRepository studentRepository,
			TeacherRepository teacherRepository,
			ProfessorRepository professorRepository,
			CollegeManagerRepository collegeManagerRepository,
			SchoolManagerRepository schoolManagerRepository) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
		this.professorRepository = professorRepository;
		this.collegeManagerRepository = collegeManagerRepository;
		this.schoolManagerRepository = schoolManagerRepository;
	}


	@Override
	public SignUpResultDto generalJoin(
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
			String address,
			boolean isApproval)  {
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
			user.setRoles(Collections.singletonList("ADMIN"));


		} else if(role.equalsIgnoreCase("student")) {
			user.setRoles(Collections.singletonList("STUDENT"));
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
			user.setRoles(Collections.singletonList("TEACHER"));
			Teacher teacher = Teacher.builder()
					.user(user)
					.name(name)
					.subject(subject)
					.phoneNumber(phoneNumber)
					.email(email)
					.schoolName(SchoolName)
					.officeNumber(officeNumber)
					.build();
			teacherRepository.save(teacher);
		} else if(role.equalsIgnoreCase("professor")) {
			user.setRoles(Collections.singletonList("PROFESSOR"));
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
	public SignUpResultDto institutionalJoin(
	        String userId,
	        String password, 
	        String officeNumber,
	        boolean isApproval,
	        String address,
	        MultipartFile proofData,
	        String managerId,
	        String role)  {
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
	        user.setRoles(Collections.singletonList("ADMIN"));

	    } else if(role.equalsIgnoreCase("schoolManager")) {
	        user.setRoles(Collections.singletonList("SCHOOL_MANAGER"));
	        
	        String proofDataFilePath;
	        try {
	            proofDataFilePath = saveFile(proofData, userId);  // userId를 넘겨주어 파일 이름을 userId로 설정
	            
	            Optional<User> userOptional = userRepository.findByUserId(managerId);
	            if (!userOptional.isPresent()) {
	                throw new IllegalArgumentException("Invalid managerId: " + managerId);
	            }
	            
	            User getUserId = userOptional.get();
	            
	            List<Teacher> teachers = teacherRepository.findByUser(getUserId);
	            if (teachers.isEmpty()) {
	                throw new IllegalArgumentException("No teachers found for user: " + managerId);
	            }
	            
	            String schoolName = teachers.get(0).getSchoolName();
	            
	            SchoolManager schoolManager = SchoolManager.builder()
	                    .user(user)
	                    .address(address)
	                    .officeNumber(officeNumber)
	                    .proofData(proofDataFilePath)
	                    .managerId(managerId)
	                    .schoolName(schoolName)
	                    .build();
	            
	            schoolManagerRepository.save(schoolManager);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    } else if(role.equalsIgnoreCase("collegeManager")) {
	        user.setRoles(Collections.singletonList("COLLEGE_MANAGER"));
	        
	        String proofDataFilePath;
	        try {
	            proofDataFilePath = saveFile(proofData, userId);  // userId를 넘겨주어 파일 이름을 userId로 설정
	            
	            Optional<User> userOptional = userRepository.findByUserId(managerId);
	            if (!userOptional.isPresent()) {
	                throw new IllegalArgumentException("Invalid managerId: " + managerId);
	            }

	            User getUserId = userOptional.get();

	            // User와 매핑된 Professor를 가져옵니다.
	            List<Professor> professors = professorRepository.findByUser(getUserId);
	            if (professors.isEmpty()) {
	                throw new IllegalArgumentException("No professors found for user: " + managerId);
	            }

	            String collegeName = professors.get(0).getCollege();

	            CollegeManager collegeManager = CollegeManager.builder()                       
	                    .user(user)
	                    .address(address)
	                    .officeNumber(officeNumber)
	                    .proofData(proofDataFilePath)
	                    .managerId(managerId)
	                    .college(collegeName)
	                    .build();
	            collegeManagerRepository.save(collegeManager);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
				.token(jwtTokenProvider.createToken(user.getId(), user.getUserId(),
						user.getRoles()))
				.build();
		LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
		setSuccessResult(signInResultDto);

		return signInResultDto;	
	}
	
	private String saveFile(MultipartFile file, String userId) throws FileUploadException {
	    if (file.isEmpty()) {
	        throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
	    }

	    // 파일 이름을 userId로 설정 (원래 파일 이름을 사용하지 않고, userId를 사용)
	    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename()); // 원래 파일 이름을 얻을 수 있으나 사용하지 않음
	    String uniqueFileName = userId + "_" + UUID.randomUUID().toString();  // userId와 UUID를 결합하여 고유한 파일명 생성
	    Path uploadDirPath = Paths.get(uploadDir);
	    Path filePath = uploadDirPath.resolve(uniqueFileName); // userId를 파일 이름으로 사용

	    try {
	        if (!Files.exists(uploadDirPath)) {
	            Files.createDirectories(uploadDirPath);
	        }
	        Files.copy(file.getInputStream(), filePath);  // 파일 저장
	        LOGGER.info("파일 업로드 성공: {}", filePath);
	        return filePath.toString();
	    } catch (IOException e) {
	        LOGGER.error("파일 업로드 실패: {}", originalFileName, e);
	        throw new FileUploadException("파일 업로드 중 오류 발생", e);
	    }
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
