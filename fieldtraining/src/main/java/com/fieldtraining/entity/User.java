package com.fieldtraining.entity;

import com.fieldtraining.constant.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FIELD_USER")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // STUDENT, TEACHER, PROFESSOR

    private boolean okay;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student studentDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacherDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Professor professorDetail;
}
