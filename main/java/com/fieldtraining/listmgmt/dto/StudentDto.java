package com.fieldtraining.listmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
public class StudentDto {
    private String name;
    private String college;
    private String department;
    private String email;
    private String subject;
    private int studentNumber;
}
