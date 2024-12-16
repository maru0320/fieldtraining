package com.fieldtraining.ReferenceBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.ReferenceBoard.entity.ReferenceComment;


public interface ReferenceCommentRepository extends JpaRepository<ReferenceComment, Long> {
}
