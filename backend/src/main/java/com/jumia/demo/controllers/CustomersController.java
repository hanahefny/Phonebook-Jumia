package com.jumia.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomersController.class);

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CountryService countryService;

  private static final String DEFAULT_PAGE_COUNT = "10";

  @GetMapping
  public ResponseEntity<PaginatedCustomerResponse> getCustomers(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int itemsPerPage,
      @RequestParam(required = false) String country,
      @RequestParam(required = false) Boolean isValid) {
    if (page < 0) {
      LOGGER.warn("Page index cannot be less than zero!");
      page = 0;
    }
    if (itemsPerPage <= 1) {
      LOGGER.warn("Page items cannot be less than one!");
      itemsPerPage = Integer.valueOf(DEFAULT_PAGE_COUNT);
    }
    return ResponseEntity.ok(
        customerService.findAndFilterCustomers(FilterAndPaginationUtils.getModel(page, itemsPerPage, country, isValid)));
  }

  @GetMapping("/countries")
  public List<String> getAllCountries() {
    return countryService.getCountries();
  }
}
