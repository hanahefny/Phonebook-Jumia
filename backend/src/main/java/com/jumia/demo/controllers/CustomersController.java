package com.jumia.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jumia.demo.models.PaginatedCustomerResponse;
import com.jumia.demo.services.CountryService;
import com.jumia.demo.services.CustomerService;
import com.jumia.demo.utils.FilterAndPaginationUtils;

@RestController
@CrossOrigin
@RequestMapping(path = "/customers")
public class CustomersController {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CountryService countryService;

  @GetMapping
  public ResponseEntity<PaginatedCustomerResponse> getCustomers(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int itemsPerPage,
      @RequestParam(required = false) String country,
      @RequestParam(required = false) Boolean isValid) {
    return ResponseEntity.ok(
        customerService.findAndFilterCustomers(FilterAndPaginationUtils.getModel(page, itemsPerPage, country, isValid)));
  }

  @GetMapping("/countries")
  public List<String> getAllCountries() {
    return countryService.getCountries();
  }
}
