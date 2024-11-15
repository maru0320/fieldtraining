package com.fieldtraining.PracticeBoard.entity;

import java.util.Date;

import com.fieldtraining.entity.User;

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
public class PracticeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long training_number;
	
	private String title;

	private String fileName;
	
	private String content;
	
	private Date training_date;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="user_id")
	private User writer;
	
	

}
