package com.springboot.relationship.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.relationship.data.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
