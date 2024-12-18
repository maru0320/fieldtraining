package com.fieldtraining.managerInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fieldtraining.data.entity.SchoolManager;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolManager, Long>{

}
