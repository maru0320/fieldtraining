package com.fieldtraining.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student {

    @Id
    private Long id; // User의 ID를 재사용

    private String name;

    private String college;

    private String department;

    private String email;

    private String phoneNumber;

    private String schoolName;
    
    private String subject;

    private int studentNumber;

    @OneToOne
    @MapsId // User의 ID를 기본 키로 매핑
    @JoinColumn(name = "id") // 기본 키이자 외래 키로 사용
    private User user;
}