package com.fieldtraining.FormBoard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FormFileDto {
	
    private Long id; 

    private String originalName; 

    private String storedPath; 

    private Long fileSize; 

    private String uploadTime; 
    
}
