package com.jumia.demo.models;

    import lombok.Builder;
    import lombok.Data;
    import java.util.List;
@Data
@Builder
public class PaginatedCustomerResponse {
  private List<Customer> Customers;
  private Long numberOfItems;
  private int numberOfPages;
}