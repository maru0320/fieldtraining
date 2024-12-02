package com.packt.fieldtraining.Listmgmt.dto;

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
public class TeacherDto {
    private String name;
    private String email;
    private String schoolName;
    private String subject;
}
