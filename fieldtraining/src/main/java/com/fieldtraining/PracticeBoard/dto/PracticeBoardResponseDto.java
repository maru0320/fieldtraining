package com.fieldtraining.PracticeBoard.dto;

import java.time.LocalDateTime;

import com.fieldtraining.constant.Role;
import com.fieldtraining.entity.User;

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
public class PracticeBoardResponseDto {
	
	private Long boardID;          // PracticeBoard ID
    private String title;
    private String writerName;    // 작성자 이름
    private LocalDateTime trainingDate;

}
