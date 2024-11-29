package com.springboot.relationship.data.repository.support;

import java.util.List;

import com.springboot.relationship.data.entity.Product;

public interface ProductRepositoryCustom {

	List<Product> findByName(String name);
}
