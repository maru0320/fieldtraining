package com.fieldtraining.UnionBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.UnionBoard.entity.UnionComment;


public interface UnionCommentRepository extends JpaRepository<UnionComment, Long> {
}
