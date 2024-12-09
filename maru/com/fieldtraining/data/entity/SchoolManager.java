package com.fieldtraining.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SCHOOL_MANAGER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolManager {

    @Id
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String officeNumber;

    @Column(nullable = false)
    private String proofData;

    @Column(nullable = false)
    private String managerId; 
    
    @Column(nullable = false)
    private String schoolName; 
    
    // User와의 기본 키 공유 설정
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

}