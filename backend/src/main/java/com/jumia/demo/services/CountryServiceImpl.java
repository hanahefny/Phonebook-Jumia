package com.jumia.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {
  @Autowired
  private CountryRegistry countryRegistry;

  @Override
  public List<String> getCountries() {
    return countryRegistry.getCountryNames();
  }
}
