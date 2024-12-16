package com.fieldtraining.NoticeBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor 
public class CommentRequestDto {
	
	private Long boardID;
	
	private Long userID;
	
	private String content;

}
