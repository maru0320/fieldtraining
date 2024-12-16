package com.fieldtraining.ReferenceBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.ReferenceBoard.entity.ReferenceFile;


public interface ReferenceFileRepository extends JpaRepository<ReferenceFile, Long> {
}
