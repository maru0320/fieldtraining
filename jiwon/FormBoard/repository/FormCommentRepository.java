package com.fieldtraining.FormBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.FormBoard.entity.FormComment;


public interface FormCommentRepository extends JpaRepository<FormComment, Long> {
}
