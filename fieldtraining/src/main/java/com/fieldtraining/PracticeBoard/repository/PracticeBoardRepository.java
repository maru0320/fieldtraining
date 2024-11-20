package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.PracticeBoard.entity.PracticeBoard;


public interface PracticeBoardRepository extends JpaRepository<PracticeBoard, Long> {
}
