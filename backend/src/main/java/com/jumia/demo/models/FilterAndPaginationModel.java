package com.jumia.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//Object to be sent to Service instead of sending all params to every method
public class FilterAndPaginationModel {
  private int page;
  private int itemsPerPage;
  private String country;
  private Boolean isValid;
}
