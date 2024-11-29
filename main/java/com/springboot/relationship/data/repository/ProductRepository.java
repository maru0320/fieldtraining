package com.springboot.relationship.data.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.relationship.data.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{
   // find...By
   Optional<Product>findByNumber(Long number);
   List<Product> findAllByName(String name);
   Product queryBynumber(Long number);
   
   List<Product> findFirstsByName(String name);
   List<Product> findTop10ByName(String name);
   
   //findByNumber 메서드와 동일하게 동작
   Product findByNumberIs(Long number);
   Product findByNumberEquals(Long number);
   
   Product findByNumberIsNot(Long number);
   Product findByNumberNot(Long number);

   List<Product> findByUpdatedAtNull();
   List<Product> findByUpdatedAtIsNull();
   List<Product> findByUpdatedAtNotNull();
   List<Product> findByUpdatedAtIsNotNull();
   
   Product findByNumberAndName(Long number, String name);
   Product findByNumberOrName(Long number, String name);

   List<Product> findByPriceIsGreaterThan(Long price);
   List<Product> findByPriceGreaterThan(Long price);
   List<Product> findByPriceGreaterThanEqual(Long price);
   List<Product> findByPriceIsLessThan(Long price);
   List<Product> findByPriceLessThan(Long price);
   List<Product> findByPriceLessThanEqual(Long price);
   List<Product> findByPriceIsBetween(Long lowPrice, Long highPrice);
   List<Product> findByPriceBetween(Long lowPrice, Long highPrice);
   
   List<Product> findByNameContains(String name);
   List<Product> findByNameContaining(String name);
   List<Product> findByNameIsContaining(String name);
   List<Product> findByNameStartsWith(String name);
   List<Product> findByNameStartingWith(String name);
   List<Product> findByNameIsStartingWith(String name);
   List<Product> findByNameEndsWith(String name);
   List<Product> findByNameEndingWith(String name);
   List<Product> findByNameIsEndingWith(String name);
   
   List<Product> findByName(String name, Sort sort);
   
   @Query("SELECT p FROM Product AS p WHERE p.name = ?1")
   List<Product> findByName(String name);

   @Query("SELECT p FROM Product p WHERE p.name = :name")
   List<Product> findByNameParam(@Param("name") String name);

   @Query("SELECT p.name, p.price, p.stock FROM Product p WHERE p.name = :name")
   List<Object[]> findByNameParam2(@Param("name") String name);
   
   
   
   

}
