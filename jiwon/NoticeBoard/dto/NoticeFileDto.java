package com.fieldtraining.NoticeBoard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoticeFileDto {
	
    private Long id; 

    private String originalName; 

    private String storedPath; 

    private Long fileSize; 

    private String uploadTime; 
    
}
