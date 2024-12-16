package com.fieldtraining.FormBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.FormBoard.entity.FormFile;


public interface FormFileRepository extends JpaRepository<FormFile, Long> {
}
