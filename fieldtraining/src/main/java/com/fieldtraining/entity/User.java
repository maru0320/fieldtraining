package com.fieldtraining.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEACHER")
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String user_id;
	
	private String password;
	
	private String name;
	
	private String subject;
	
	private String email;
	
	private String phone_number;
	
	private boolean okay;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student studentDetail;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacherDetail;


}
