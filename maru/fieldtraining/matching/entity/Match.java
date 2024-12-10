package com.fieldtraining.matching.entity;

import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matching")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacherId", nullable = false)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.PENDING;  // 기본값을 PENDING으로 설정

    private boolean matchApproved = false;  // 기본값을 false로 설정
}

