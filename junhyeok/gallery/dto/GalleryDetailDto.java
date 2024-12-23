package com.fieldtraining.gallery.dto;

import java.time.LocalDate;
import java.util.List;

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
public class GalleryDetailDto {
	private Long boardID;

    private String title;
    
    private String writerName;

    private LocalDate date;
    
    private List<GalleryFileDto> files;
}
