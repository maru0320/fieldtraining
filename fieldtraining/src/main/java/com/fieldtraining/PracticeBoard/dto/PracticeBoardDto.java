package com.fieldtraining.PracticeBoard.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PracticeBoardDto {

	private long id;
	
	private String title;

	private String fileName;
	
	private String writer;
	
	private String content;
	
	private Date training_date;

	public PracticeBoardDto(long id, String title, String fileName, String writer, String content, Date training_date) {
	
		this.id = id;
		this.title = title;
		this.fileName = fileName;
		this.writer = writer;
		this.content = content;
		this.training_date = training_date;
	}

	
	
	
	
	
	
}
