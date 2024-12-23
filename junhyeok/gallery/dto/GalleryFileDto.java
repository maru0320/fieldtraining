package com.fieldtraining.gallery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GalleryFileDto {
	private Long id; 

    private String originalName; 

    private String imgUrl; 

    private Long fileSize; 
}
