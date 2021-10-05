package com.jumia.demo.services;

import com.jumia.demo.models.FilterAndPaginationModel;
import com.jumia.demo.models.PaginatedCustomerResponse;

public interface CustomerService {
  /***
   *
   * @param filterAndPaginationModel model with filter and paging properties
   * @return PaginatedCustomerResponse with Customers in page, total number of customers and total number of pages
   */
  PaginatedCustomerResponse findAndFilterCustomers(FilterAndPaginationModel filterAndPaginationModel);

}
