package com.fieldtraining.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
public class Student {
	
	private String id;
	
	private String college;
	
	private String department;

	private int student_number;
	
	private String school_name;
	
	@OneToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="id")
	private User user;
	
	

}
