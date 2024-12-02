package com.fieldtraining.PracticeBoard.dto;

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
public class PracticeBoardDetailDto {

    private Long boardID;

    private String title;

    private String content;
    
    private String writerName;

    private String trainingDate;
    
    private List<PracticeFileDto> files;

}
