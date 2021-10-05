package com.jumia.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jumia.demo.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  
  Page<Customer> findByPhoneStartsWith(String code, Pageable pageable);

  List<Customer> findByPhoneStartsWith(String code);
}
