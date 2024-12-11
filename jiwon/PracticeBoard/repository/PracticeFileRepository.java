package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.PracticeBoard.entity.PracticeFile;


public interface PracticeFileRepository extends JpaRepository<PracticeFile, Long> {
}
