package com.packt.fieldtraining.calendar.entity;

import java.time.LocalDate;

import com.packt.fieldtraining.common.entity.User;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "calendar")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate start;

    @Column(nullable = true)
    private LocalDate end;
    
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자 정보 (Role 포함)
}
