package com.fieldtraining.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fieldtraining.config.security.JwtTokenProvider;
import com.fieldtraining.data.dto.FindUserIdRequestDto;
import com.fieldtraining.data.dto.SignInRequestDto;
import com.fieldtraining.data.dto.SignInResultDto;
import com.fieldtraining.data.dto.SignUpRequestDto;
import com.fieldtraining.data.dto.SignUpResultDto;
import com.fieldtraining.service.SignService;
import com.fieldtraining.service.TokenBlacklistService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SignController(SignService signService, TokenBlacklistService tokenBlacklistService, JwtTokenProvider jwtTokenProvider) {
        this.signService = signService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 로그인 엔드포인트
    @PostMapping("/login")  // 절대 경로를 상대 경로로 수정
    public ResponseEntity<SignInResultDto> login(@RequestBody SignInRequestDto signInRequestDto) {
        String userId = signInRequestDto.getUserId();
        String password = signInRequestDto.getPassword();

        LOGGER.info("[login] 로그인을 시도하고 있습니다. id : {}", userId);

        // 로그인 서비스 호출
        try {
            SignInResultDto signInResultDto = signService.login(userId, password);

            if (signInResultDto.getCode() == 0) {
                LOGGER.info("[login] 정상적으로 로그인되었습니다. id: {}, token : {}", userId, signInResultDto.getToken());
                return ResponseEntity.ok(signInResultDto);  // 로그인 성공 시 200 OK 반환
            } else {http://localhost:3000?logout
                LOGGER.warn("[login] 로그인 실패 id: {}", userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(signInResultDto);  // 인증 실패 시 401 Unauthorized
            }
        } catch (RuntimeException e) {
            LOGGER.error("[login] 로그인 중 오류 발생 id: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 예외 처리 시 500 Internal Server Error 반환
        }
    }
    
    // 회원가입 엔드포인트
    @PostMapping("/join/generaljoin")  // 절대 경로를 상대 경로로 수정
    public ResponseEntity<SignUpResultDto> generalJoin(@RequestBody SignUpRequestDto signUpRequestDto) {
        LOGGER.info("[signUp] 회원가입을 수행합니다. id = {}, name = {}, role = {}",
                signUpRequestDto.getUserId(),
                signUpRequestDto.getName(),
                signUpRequestDto.getRole());

        // 회원가입 서비스 호출
        try {
            SignUpResultDto signUpResultDto = signService.generalJoin(
                    signUpRequestDto.getUserId(),
                    signUpRequestDto.getName(),
                    signUpRequestDto.getPassword(),
                    signUpRequestDto.getRole(),
                    signUpRequestDto.getCollege(),
                    signUpRequestDto.getDepartment(),
                    signUpRequestDto.getStudentNumber(),
                    signUpRequestDto.getSubject(),
                    signUpRequestDto.getEmail(),
                    signUpRequestDto.getPhoneNumber(),
                    signUpRequestDto.getSchoolName(),
                    signUpRequestDto.getOfficeNumber(),
                    signUpRequestDto.getAddress(),
                    signUpRequestDto.isApproval()
            );

            LOGGER.info("[signUp] 회원가입 완료. id : {}", signUpRequestDto.getUserId());

            if (signUpResultDto.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(signUpResultDto);  // 회원가입 성공 시 201 Created 반환
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signUpResultDto);  // 실패 시 400 Bad Request 반환
            }
        } catch (Exception e) {
            LOGGER.error("[signUp] 회원가입 중 오류 발생. id = {}", signUpRequestDto.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 예외 발생 시 500 Internal Server Error 반환
        }
    }
    
    @PostMapping(value = "/join/institutionaljoin", consumes = "multipart/form-data")
    public ResponseEntity<SignUpResultDto> institutionalJoin(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("officeNumber") String officeNumber,
            @RequestParam("address") String address,
            @RequestParam("isApproval") boolean isApproval,
            @RequestParam(value = "proofData", required = false) MultipartFile proofData,            
            @RequestParam("managerId") String managerId) {
        
        LOGGER.info("[signUp] 회원가입을 수행합니다. id = {}, role = {}", userId, role);

        // 회원가입 서비스 호출
        try {
            SignUpResultDto signUpResultDto = signService.institutionalJoin(
                    userId,
                    password,
                    officeNumber,
                    isApproval,
                    address,
                    proofData,
                    managerId,
                    role
            );

            LOGGER.info("[signUp] 회원가입 완료. id : {}", userId);

            if (signUpResultDto.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(signUpResultDto);  // 회원가입 성공 시 201 Created 반환
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signUpResultDto);  // 실패 시 400 Bad Request 반환
            }
        } catch (Exception e) {
            LOGGER.error("[signUp] 회원가입 중 오류 발생. id = {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 예외 발생 시 500 Internal Server Error 반환
        }
    }
    
    @PostMapping("/find/Id") 
    public ResponseEntity<String> findUserId(@RequestBody FindUserIdRequestDto requestDto) {
    	try {
    		String userId = signService.findUserIdByNameAndEmail(requestDto.getName(), requestDto.getEmail());
    		return ResponseEntity.ok(userId);
    	} catch (RuntimeException e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    	}
    }
    
    // 승인상태확인
    @GetMapping("/isapproval/check/{userId}")
    public ResponseEntity<Map<String, Boolean>> checkIsApproval(@PathVariable Long userId) {
    	boolean isApproval = signService.checkIsApproval(userId);
    	Map<String, Boolean> response = new HashMap<>();
    	response.put("isApproval", isApproval);
    	return ResponseEntity.ok(response);
    }

}
