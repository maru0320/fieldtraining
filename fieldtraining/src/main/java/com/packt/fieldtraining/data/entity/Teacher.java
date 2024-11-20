package com.packt.fieldtraining.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEACHER")
@Getter
@Setter
@Builder
public class Teacher {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private String schoolName;

	@Column(nullable = false)
	private String officeNumber;


	@OneToOne
	@MapsId
	@JoinColumn(name="teacherId")
	private User user;


}