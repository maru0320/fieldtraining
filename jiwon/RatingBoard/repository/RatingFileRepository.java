package com.fieldtraining.RatingBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.RatingBoard.entity.RatingFile;


public interface RatingFileRepository extends JpaRepository<RatingFile, Long> {
}
