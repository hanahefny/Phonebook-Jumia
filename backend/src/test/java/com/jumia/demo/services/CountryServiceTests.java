package com.jumia.demo.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureDataJpa
public class CountryServiceTests {

  @Autowired
  private CountryService countryService;

  @Autowired
  private CountryRegistry countryRegistry;

  @Test
  public void testFetchAllCountries() {
    //Given

    //When
    List<String> countries = countryService.getCountries();

    //Then
    Assert.assertEquals(5, countries.size());

  }

  @Test
  public void testRetrievingCountryCode() {
    //Given

    //When
    String code = countryRegistry.getCountryCode("Uganda");

    //Then
    Assert.assertEquals("256", code);

  }

}
