package com.fieldtraining.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fieldtraining.matching.entity.Match;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEACHER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {


	@Id
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private String schoolName;

	@Column(nullable = false)
	private String officeNumber;
	
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String phoneNumber;

	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name="id")
	private User user;
	

}