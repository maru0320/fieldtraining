package com.fieldtraining.managerInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.data.entity.User;

public interface UserUpdateRepository extends JpaRepository<User, Long>{
}
