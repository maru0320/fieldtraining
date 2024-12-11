package com.fieldtraining.OperationBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.OperationBoard.entity.OperationComment;


public interface OperationCommentRepository extends JpaRepository<OperationComment, Long> {
}
