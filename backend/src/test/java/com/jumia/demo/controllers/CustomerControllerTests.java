package com.jumia.demo.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jumia.demo.models.Customer;
import com.jumia.demo.repository.CustomerRepository;
import com.jumia.demo.services.CountryRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class CustomerControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private CustomerRepository repository;

  @Autowired
  private CountryRegistry countryRegistry;

  @Before
  public void setUp() {
    // Creating differet cutomers with different validity and countries to be used in the next tests
    repository.save(new Customer(1l, "Valid Cameroon", "(237) 697151594"));
    repository.save(new Customer(2l, "Valid Morocco", "(212) 698054317"));
    repository.save(new Customer(3l, "Valid Uganda", "(256) 775069443"));
    repository.save(new Customer(4l, "Invalid Uganda", "(256) 7503O6263"));
    repository.save(new Customer(5l, "Invalid Mozambique", "(258) 84330678235"));
    repository.save(new Customer(6l, "Invalid Ethiopia", "(251) 9119454961"));

  }

  @Test
  public void testFindAll() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Fetching data without filtering
    // THEN
    // All data must be returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("6"));
  }

  @Test
  public void testFilterWithCountry() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Fetching data with Country Filter 'Uganda'
    // THEN
    // 2 customers in Uganda are returned (both valid and Invalid)
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&country=Uganda")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("2"))
        .andExpect(jsonPath("$.customers[0].valid").value("true"))
        .andExpect(jsonPath("$.customers[0].countryName").value("Uganda"))
        .andExpect(jsonPath("$.customers[1].valid").value("false"))
        .andExpect(jsonPath("$.customers[1].countryName").value("Uganda"));
  }

  @Test
  public void testFilterValidNumbers() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Fetching data with Validity=true
    // THEN
    // All valid numbers in all countries are returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&isValid=true")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("3"))
        .andExpect(jsonPath("$.customers[0].id").value(1l))
        .andExpect(jsonPath("$.customers[0].valid").value("true"))
        .andExpect(jsonPath("$.customers[0].countryName").value("Cameroon"))
        .andExpect(jsonPath("$.customers[1].id").value(2l))
        .andExpect(jsonPath("$.customers[1].valid").value("true"))
        .andExpect(jsonPath("$.customers[1].countryName").value("Morocco"))
        .andExpect(jsonPath("$.customers[2].id").value(3l))
        .andExpect(jsonPath("$.customers[2].valid").value("true"))
        .andExpect(jsonPath("$.customers[2].countryName").value("Uganda"));
  }

  @Test
  public void testFilterInValidNumbers() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Fetching data with Validity=false
    // THEN
    // All invalid numbers in all countries are returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&isValid=false")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("3"))
        .andExpect(jsonPath("$.customers[0].id").value(4l))
        .andExpect(jsonPath("$.customers[0].valid").value("false"))
        .andExpect(jsonPath("$.customers[0].countryName").value("Uganda"))
        .andExpect(jsonPath("$.customers[1].id").value(5l))
        .andExpect(jsonPath("$.customers[1].valid").value("false"))
        .andExpect(jsonPath("$.customers[1].countryName").value("Mozambique"))
        .andExpect(jsonPath("$.customers[2].id").value(6l))
        .andExpect(jsonPath("$.customers[2].valid").value("false"))
        .andExpect(jsonPath("$.customers[2].countryName").value("Ethiopia"));
  }

  @Test
  public void testCountryFilterForValidNumbers() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Filter data with country= Uganda and Validity=true
    // THEN
    // All valid numbers in Uganda are returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&country=Uganda&isValid=true")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("1"))
        .andExpect(jsonPath("$.customers[0].id").value(3l))
        .andExpect(jsonPath("$.customers[0].valid").value("true"))
        .andExpect(jsonPath("$.customers[0].countryName").value("Uganda"));
  }

  @Test
  public void testCountryFilterForInValidNumbers() throws Exception {
    //Given
    //Data already initialized in before method
    // WHEN
    // Filter data with country= Uganda and Validity=false
    // THEN
    // All invalid numbers in Uganda are returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&country=Uganda&isValid=false")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfItems").value("1"))
        .andExpect(jsonPath("$.customers[0].id").value(4l))
        .andExpect(jsonPath("$.customers[0].valid").value("false"))
        .andExpect(jsonPath("$.customers[0].countryName").value("Uganda"));
  }

  @Test
  public void testFilterWithNonExistingCountry() throws Exception {
    // GIVEN
    // No Customer has country Germany
    // WHEN
    // Fetching data with filtering country = Germany
    // THEN
    // An empty result set is returned
    mvc.perform(get("/api/customers/getCustomers?page=1&itemsPerPage=10&country=Germany")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customers").isEmpty());
  }

  @Test
  public void testFetchAllCounties() throws Exception {
    // GIVEN
    // There exists 5 countries in the existing Country Code LookUp cache
    // WHEN
    // Fetching data for available countries
    // THEN
    // Five countries are returned, matching the lookup cache data
    Assert.assertEquals(5, countryRegistry.getCountryNames().size());

    mvc.perform(get("/api/customers/getCountries/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(5)));
  }
}
