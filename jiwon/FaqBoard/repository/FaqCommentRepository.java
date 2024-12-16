package com.fieldtraining.FaqBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.FaqBoard.entity.FaqComment;


public interface FaqCommentRepository extends JpaRepository<FaqComment, Long> {
}
