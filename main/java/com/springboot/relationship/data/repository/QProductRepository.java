package com.springboot.relationship.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.springboot.relationship.data.entity.Product;

public interface QProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product>{

}
