package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.PracticeBoard.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
