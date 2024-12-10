package com.fieldtraining.ClassBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.ClassBoard.entity.ClassComment;


public interface ClassCommentRepository extends JpaRepository<ClassComment, Long> {
}
