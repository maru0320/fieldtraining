package com.springboot.relationship.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.relationship.data.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long>{

}
