package com.fieldtraining.NoticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.NoticeBoard.entity.NoticeComment;


public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
}
