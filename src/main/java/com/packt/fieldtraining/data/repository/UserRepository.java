package com.packt.fieldtraining.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.packt.fieldtraining.data.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
