package com.packt.fieldtraining.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.common.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}