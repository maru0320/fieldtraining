package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.PracticeBoard.entity.PracticeComment;


public interface CommentRepository extends JpaRepository<PracticeComment, Long> {
}
