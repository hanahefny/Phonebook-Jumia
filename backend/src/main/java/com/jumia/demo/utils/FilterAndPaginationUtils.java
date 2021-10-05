package com.jumia.demo.utils;

import com.jumia.demo.models.FilterAndPaginationModel;

public class FilterAndPaginationUtils {
  public static FilterAndPaginationModel getModel(int page, int itemsPerPage, String country, Boolean isValid) {
    return new FilterAndPaginationModel(page, itemsPerPage, country, isValid);
  }
}
