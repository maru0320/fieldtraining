package com.packt.fieldtraining.ManagerInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.common.entity.Manager;

public interface ManagerInfoRepository extends JpaRepository<Manager, Long>{

}
