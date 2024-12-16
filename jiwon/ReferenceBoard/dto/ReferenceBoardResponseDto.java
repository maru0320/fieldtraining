package com.fieldtraining.ReferenceBoard.dto;

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
public class ReferenceBoardResponseDto {
	
	private Long boardID;          // PracticeBoard ID
    private String title;
    private String writerName;    // 작성자 이름
    private String date;
    private String writerRole;
}
