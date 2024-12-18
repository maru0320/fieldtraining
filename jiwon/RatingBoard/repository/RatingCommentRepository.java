package com.fieldtraining.RatingBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.RatingBoard.entity.RatingComment;


public interface RatingCommentRepository extends JpaRepository<RatingComment, Long> {
}
