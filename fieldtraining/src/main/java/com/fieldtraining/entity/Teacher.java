package com.fieldtraining.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEACHER")
@Getter
@Setter
public class Teacher {
	
	private String id;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="user_id")
	private User user;

}
