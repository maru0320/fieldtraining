package com.fieldtraining.NoticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.NoticeBoard.entity.NoticeFile;


public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
}
