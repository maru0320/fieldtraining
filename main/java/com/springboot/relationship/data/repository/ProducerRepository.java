package com.springboot.relationship.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.relationship.data.entity.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long>{

}
