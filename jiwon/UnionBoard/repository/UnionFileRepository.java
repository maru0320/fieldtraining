package com.fieldtraining.UnionBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.UnionBoard.entity.UnionFile;


public interface UnionFileRepository extends JpaRepository<UnionFile, Long> {
}
