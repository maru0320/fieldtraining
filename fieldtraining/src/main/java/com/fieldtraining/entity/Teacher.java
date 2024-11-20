package com.fieldtraining.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Teacher {
	
	@Id
	private Long id;
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String schoolName;
	
	private String subject;
	
	private String officeNumber;
	
	@OneToOne
	@MapsId // User ID를 Student의 기본 키로 사용
	@JoinColumn(name = "id") // 기본 키이자 외래 키로 사용
	private User user;

}
