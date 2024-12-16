package com.fieldtraining.FaqBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.FaqBoard.entity.FaqFile;


public interface FaqFileRepository extends JpaRepository<FaqFile, Long> {
}
