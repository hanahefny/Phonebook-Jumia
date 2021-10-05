package com.jumia.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jumia.demo.models.Customer;
import com.jumia.demo.models.FilterAndPaginationModel;
import com.jumia.demo.models.PaginatedCustomerResponse;

@Service
public class CustomerServiceImpl implements CustomerService {
  @Autowired
  private CustomerQueryComponent customerQueryComponent;

  @Override
  public PaginatedCustomerResponse findAndFilterCustomers(FilterAndPaginationModel filterAndPaginationModel) {
    Page<Customer> pagedResult = customerQueryComponent.getCustomers(filterAndPaginationModel);

    return PaginatedCustomerResponse.builder()
        .numberOfItems(pagedResult.getTotalElements())
        .numberOfPages(pagedResult.getTotalPages())
        .Customers(pagedResult.getContent())
        .build();
  }
}
