package com.fieldtraining.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final StudentRepository studentRepository;
	
	@Autowired
	private final TeacherRepository teacherRepository;
	
	@Autowired
	private final ProfessorRepository professorRepository;
	
	@Autowired
	private final CollegeManagerRepository collegeManagerRepository;
	
	@Autowired 
	private final SchoolManagerRepository schoolManagerRepository;
	
	


	

	public AdminController(UserRepository userRepository, StudentRepository studentRepository,
			TeacherRepository teacherRepository, ProfessorRepository professorRepository,
			CollegeManagerRepository collegeManagerRepository, SchoolManagerRepository schoolManagerRepository) {
		super();
		this.userRepository = userRepository;
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
		this.professorRepository = professorRepository;
		this.collegeManagerRepository = collegeManagerRepository;
		this.schoolManagerRepository = schoolManagerRepository;
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
	    List<User> users = userRepository.findAll();
	    for (User user : users) {
	        if ("student".equals(user.getRole())) {
	            Optional<Student> student = studentRepository.findById(user.getId());
	            student.ifPresent(s -> user.setStudentDetail(s)); // 학생 세부 정보 설정
	        } else if ("teacher".equals(user.getRole())) {
	            Optional<Teacher> teacher = teacherRepository.findById(user.getId());
	            teacher.ifPresent(t -> user.setTeacherDetail(t)); // 교사 세부 정보 설정
	        } else if ("professor".equals(user.getRole())) {
	            Optional<Professor> professor = professorRepository.findById(user.getId());
	            professor.ifPresent(p -> user.setProfessorDetail(p)); // 교수 세부 정보 설정
	        } else if ("collegeManager".equals(user.getRole())) {
	            Optional<CollegeManager> collegeManager = collegeManagerRepository.findById(user.getId());
	            collegeManager.ifPresent(cm -> user.setCollegeManagerDetail(cm)); // 기관 관리자 세부 정보 설정
	        } else if ("schoolManager".equals(user.getRole())) {
	            Optional<SchoolManager> schoolManager = schoolManagerRepository.findById(user.getId());
	            schoolManager.ifPresent(sm -> user.setSchoolManagerDetail(sm)); // 기관 관리자 세부 정보 설정
	        }
	    }
	    return ResponseEntity.ok(users);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id){
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@PutMapping("/users/{id}/approval")
	public ResponseEntity<String> approveUser(@PathVariable Long id, @RequestParam boolean isApproved) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setApproval(isApproved);
			userRepository.save(user);
			return ResponseEntity.ok("User approval status updated.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return ResponseEntity.ok("User deleted.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
}
