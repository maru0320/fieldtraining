package com.fieldtraining.NoticeBoard.dto;

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
public class NoticeBoardDetailDto {

    private Long boardID;
    
    private Long writerID;

    private String title;

    private String content;
    
    private String writerName;

    private String date;
    
    private List<NoticeFileDto> files;
    	
    private List<CommentResponseDto> comments;

}
