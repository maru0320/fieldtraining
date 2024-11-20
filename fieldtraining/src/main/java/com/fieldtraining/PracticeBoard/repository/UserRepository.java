package com.fieldtraining.PracticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
