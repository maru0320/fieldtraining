package com.fieldtraining.Matching.entity;

import com.fieldtraining.entity.Student;
import com.fieldtraining.entity.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Matching {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long matching_number;
	
	private String state;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="student_id") 
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	

}
