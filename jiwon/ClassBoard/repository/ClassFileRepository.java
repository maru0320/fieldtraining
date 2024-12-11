package com.fieldtraining.ClassBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.ClassBoard.entity.ClassFile;


public interface ClassFileRepository extends JpaRepository<ClassFile, Long> {
}
