package com.jumia.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.jumia.demo.models.Country;
import com.jumia.demo.models.Customer;
import com.jumia.demo.models.FilterAndPaginationModel;
import com.jumia.demo.repository.CustomerRepository;

@Component
public class CustomerQueryComponent {

  @Autowired
  private CustomerRepository customersRepository;

  @Autowired
  private CountryRegistry countryRegistry;

  /**
   * In case no filter it returns all customers in current page
   * In case of Filtering with countryName filtering and paging can be done from database
   * In case of Filtering with validity (transient field not stored in db) the data is filter on server side
   * In case of Filtering with validity paging is also done in server side
   * Server-side validity: records will be filtered for validity and their number may be less than the number required in the page
   * @param filterAndPaginationModel Model with page and filter parameters
   * @return A page of the customer information
   */
  public Page<Customer> getCustomers(FilterAndPaginationModel filterAndPaginationModel) {
    int pageNumber = filterAndPaginationModel.getPage() - 1;

    Pageable pageable = PageRequest.of(pageNumber, filterAndPaginationModel.getItemsPerPage());
    String country = filterAndPaginationModel.getCountry();
    boolean isFilterByValidity = filterAndPaginationModel.getIsValid() != null;
    boolean isFilterByCountry = country != null && !country.isEmpty();

    if (!isFilterByValidity && !isFilterByCountry) {
      return getAllCustomers(pageable);
    } else if (!isFilterByValidity && isFilterByCountry) {
      return getFilteredCustomersByCountry(pageable, filterAndPaginationModel.getCountry());
    } else if (isFilterByValidity && !isFilterByCountry) {
      return getFilteredCustomersByValidity(pageable, filterAndPaginationModel.getIsValid());
    }
    else {
      return getFilteredCustomersByCountryAndValidity(pageable, filterAndPaginationModel.getCountry(),
          filterAndPaginationModel.getIsValid());
    }
  }

  /***
   * Gets Page of customers
   * Set the country and validity for each customer
   * @param pageable
   * @return Page of customer
   */
  private Page<Customer> getAllCustomers(Pageable pageable) {
    Page<Customer> pagedResult = customersRepository.findAll(pageable);
    setCountryAndValidity(pagedResult.getContent());
    return pagedResult;
  }

  /**
   * Retrieves all customers from db
   * Set the country and validity for each customer
   * Server-side filtering for validity
   * Server-side paging after filtering
   * @param pageable
   * @param isValid
   * @return
   */
  private Page<Customer> getFilteredCustomersByValidity(Pageable pageable, boolean isValid) {
    List<Customer> customersWithoutPagination = customersRepository.findAll();
    setCountryAndValidity(customersWithoutPagination);
    return getFilteredByValidityCustomerPage(customersWithoutPagination, isValid, pageable);
  }

  /**
   * Paging and country filtering from database
   * Set the country and validity for each customer
   * @param pageable
   * @param country
   * @return
   */
  private Page<Customer> getFilteredCustomersByCountry(Pageable pageable, String country) {
    String countryCode = "(" + countryRegistry.getCountryCode(country) + ")";
    Page<Customer> pagedResult = customersRepository.findByPhoneStartsWith(countryCode, pageable);
    setCountryAndValidity(pagedResult.getContent());
    return pagedResult;
  }

  /**
   * Filter by country from database
   * Set the country and validity for each customer
   * Filter by validity server-side
   * Server-side paging after filtering
   * @param pageable
   * @param country
   * @param isValid
   * @return
   */
  private Page<Customer> getFilteredCustomersByCountryAndValidity(Pageable pageable, String country, boolean isValid) {
    String countryCode = "(" + countryRegistry.getCountryCode(country) + ")";
    List<Customer> customersWithoutPagination = customersRepository.findByPhoneStartsWith(countryCode);
    setCountryAndValidity(customersWithoutPagination);
    return getFilteredByValidityCustomerPage(customersWithoutPagination, isValid, pageable);
  }

  /***
   * Set the country and validity to a customer based on the initialized map and customer's phone number
   * @param customers
   */
  public void setCountryAndValidity(List<Customer> customers) {
    customers.forEach(customer -> {
      String phoneNumber = customer.getPhone();
      String countryCode =
          phoneNumber.substring(phoneNumber.indexOf("(") + 1, phoneNumber.indexOf(")"));
      Country country = countryRegistry.getCountry(countryCode);
      customer.setCountryName(country.getName());
      customer.setValid(Pattern.matches(country.getValidityRule(), customer.getPhone()));
    });
  }

  /**
   * Filters the customers list by validity
   * Then performs server-side paging
   * @param customers List to be filtered by validity
   * @param isValid
   * @param pageable
   * @return Page containing filtered customers by validity
   */
  private Page<Customer> getFilteredByValidityCustomerPage(List<Customer> customers, boolean isValid, Pageable pageable) {
    List<Customer> filteredList = filterByValidity(customers, isValid);
    List<Customer> customersInPage = getCustomersInPage(filteredList, pageable);
    return new PageImpl<Customer>(customersInPage, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
        filteredList.size());
  }

  /**
   *
   * @param customers
   * @param isValid
   * @return Filtered list by validity
   */
  private List<Customer> filterByValidity(List<Customer> customers, boolean isValid) {
    return customers.stream()
        .filter(customer -> customer.isValid() == isValid)
        .collect(Collectors.toList());
  }

  /***
   * Server-side paging required with validity filters
   * @param customers list of all customers
   * @param pageable
   * @return List of customers in the returned page
   */
  private List<Customer> getCustomersInPage(List<Customer> customers, Pageable pageable) {
    int total = customers.size();
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), total);
    List<Customer> customersInPage = new ArrayList<>();
    if (start <= end) {
      customersInPage = customers.subList(start, end);
    }
    return customersInPage;
  }
}
