package com.jumia.demo.services;

import java.util.List;

public interface CountryService {
  /**
   *
   * @return all country names stored in country registry
   */
  List<String> getCountries();
}
