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
public class Professor {
	
	@Id
	private Long id;
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String college;

	private String department;
	
	private String officeNumber;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="id")
	private User user;

}
