package com.fieldtraining.attendance.entity;

import com.fieldtraining.data.entity.Student;
import com.fieldtraining.data.entity.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId", nullable = false)
    private Teacher teacher;

    @Column(nullable = false)
    private String date;  // 출석 요청 날짜 (현재 날짜로 설정)

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status = AttendanceStatus.PENDING; // 출석 상태 (기본값: PENDING)
}