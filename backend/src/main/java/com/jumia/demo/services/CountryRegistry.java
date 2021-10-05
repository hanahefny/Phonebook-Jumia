package com.jumia.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jumia.demo.models.Country;

@Component
public class CountryRegistry {
  private final Map<String, Country> countryCache = new HashMap<>();

  public CountryRegistry() {
    initializeCache();
  }

  private void initializeCache() {
    // Setting a hard Coded map based on the given data presented in the document of the test
    // For better coding structure this should have been implemented in another way example in a json file to apply (open-close principle).
    countryCache.put("237", new Country("Cameroon", "\\(237\\)\\ ?[2368]\\d{7,8}$"));
    countryCache.put("251", new Country("Ethiopia", "\\(251\\)\\ ?[1-59]\\d{8}$"));
    countryCache.put("212", new Country("Morocco", "\\(212\\)\\ ?[5-9]\\d{8}$"));
    countryCache.put("258", new Country("Mozambique", "\\(258\\)\\ ?[28]\\d{7,8}$"));
    countryCache.put("256", new Country("Uganda", "\\(256\\)\\ ?\\d{9}$"));
  }

  /**
   * this method complexity O(n), it could have been O(1) by creating another map
   * with country value=Country name and code= CountryCode but will increase size of memory stored
   * @param countryName
   * @return Country code from map
   */
  public String getCountryCode(String countryName) {
    return countryCache.entrySet().stream()
        .filter(entry -> Objects.equals(entry.getValue().getName(), countryName))
        .map(entry -> entry.getKey())
        .findFirst()
        .orElse(null);
  }

  public Country getCountry(String countryCode) {
    return countryCache.get(countryCode);
  }

  /***
   *
   * @return All country names from map
   */
  public List<String> getCountryNames() {
    return countryCache.entrySet().stream()
        .map(entry -> entry.getValue().getName())
        .collect(Collectors.toList());
  }
}
