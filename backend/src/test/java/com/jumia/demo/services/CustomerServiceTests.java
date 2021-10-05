package com.jumia.demo.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jumia.demo.models.Customer;
import com.jumia.demo.models.FilterAndPaginationModel;
import com.jumia.demo.models.PaginatedCustomerResponse;
import com.jumia.demo.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureDataJpa
public class CustomerServiceTests {
  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerRepository repository;

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
  public void testFindAll() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, null, null);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Long total = Long.valueOf(6);
    Assert.assertEquals(2, response.getNumberOfPages());
    Assert.assertEquals(5, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(6), response.getNumberOfItems());

  }

  @Test
  public void testFilterWithCountry() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, "Uganda", null);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(1, response.getNumberOfPages());
    Assert.assertEquals(2, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(2), response.getNumberOfItems());
    //Assert for 1st returned customer info
    Customer customer1 = response.getCustomers().get(0);
    Assert.assertEquals(Long.valueOf(3), customer1.getId());
    Assert.assertEquals("Valid Uganda", customer1.getName());
    Assert.assertTrue(customer1.isValid());
    //Assert for 2nd returned customer info
    Customer customer2 = response.getCustomers().get(1);
    Assert.assertEquals(Long.valueOf(4), customer2.getId());
    Assert.assertEquals("Invalid Uganda", customer2.getName());
    Assert.assertTrue(!customer2.isValid());
  }

  @Test
  public void testFilterWithValidNumbers() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, null, true);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(1, response.getNumberOfPages());
    Assert.assertEquals(3, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(3), response.getNumberOfItems());
    //Assert for 1st returned customer info
    Customer customer1 = response.getCustomers().get(0);
    Assert.assertEquals(Long.valueOf(1), customer1.getId());
    Assert.assertEquals("Valid Cameroon", customer1.getName());
    Assert.assertTrue(customer1.isValid());
    //Assert for 2nd returned customer info
    Customer customer2 = response.getCustomers().get(1);
    Assert.assertEquals(Long.valueOf(2), customer2.getId());
    Assert.assertEquals("Valid Morocco", customer2.getName());
    Assert.assertTrue(customer2.isValid());
    //Assert for 3rd returned customer info
    Customer customer3 = response.getCustomers().get(2);
    Assert.assertEquals(Long.valueOf(3), customer3.getId());
    Assert.assertEquals("Valid Uganda", customer3.getName());
    Assert.assertTrue(customer3.isValid());
  }

  @Test
  public void testFilterWithInValidNumbers() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, null, false);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(1, response.getNumberOfPages());
    Assert.assertEquals(3, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(3), response.getNumberOfItems());
    //Assert for 1st returned customer info
    Customer customer1 = response.getCustomers().get(0);
    Assert.assertEquals(Long.valueOf(4), customer1.getId());
    Assert.assertEquals("Invalid Uganda", customer1.getName());
    Assert.assertTrue(!customer1.isValid());
    //Assert for 2nd returned customer info
    Customer customer2 = response.getCustomers().get(1);
    Assert.assertEquals(Long.valueOf(5), customer2.getId());
    Assert.assertEquals("Invalid Mozambique", customer2.getName());
    Assert.assertTrue(!customer2.isValid());
    //Assert for 3rd returned customer info
    Customer customer3 = response.getCustomers().get(2);
    Assert.assertEquals(Long.valueOf(6), customer3.getId());
    Assert.assertEquals("Invalid Ethiopia", customer3.getName());
    Assert.assertTrue(!customer3.isValid());
  }

  @Test
  public void testCountryFilterForValidNumbers() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, "Uganda", true);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(1, response.getNumberOfPages());
    Assert.assertEquals(1, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(1), response.getNumberOfItems());
    //Assert for 1st returned customer info
    Customer customer1 = response.getCustomers().get(0);
    Assert.assertEquals(Long.valueOf(3), customer1.getId());
    Assert.assertEquals("Valid Uganda", customer1.getName());
    Assert.assertTrue(customer1.isValid());
  }

  @Test
  public void testCountryFilterForInValidNumbers() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, "Uganda", false);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(1, response.getNumberOfPages());
    Assert.assertEquals(1, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(1), response.getNumberOfItems());
    //Assert for 1st returned customer info
    Customer customer1 = response.getCustomers().get(0);
    Assert.assertEquals(Long.valueOf(4), customer1.getId());
    Assert.assertEquals("Invalid Uganda", customer1.getName());
    Assert.assertTrue(!customer1.isValid());
  }

  @Test
  public void testFilterWithNonExistingCountry() {
    //Given
    FilterAndPaginationModel model = new FilterAndPaginationModel(1, 5, "Germany", false);

    //When
    PaginatedCustomerResponse response = customerService.findAndFilterCustomers(model);

    //Then
    Assert.assertEquals(0, response.getNumberOfPages());
    Assert.assertEquals(0, response.getCustomers().size());
    Assert.assertEquals(Long.valueOf(0), response.getNumberOfItems());
  }

}
