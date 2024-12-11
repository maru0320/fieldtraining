package com.fieldtraining.OperationBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.OperationBoard.entity.OperationFile;


public interface OperationFileRepository extends JpaRepository<OperationFile, Long> {
}
