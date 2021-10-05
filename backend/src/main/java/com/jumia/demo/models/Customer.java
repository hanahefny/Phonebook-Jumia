package com.jumia.demo.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "customer")
@Data
@NoArgsConstructor
public class Customer implements Serializable {
  @Id
  private Long id;

  private String name;

  private String phone;

  @Transient
  private boolean isValid;

  @Transient
  private String countryName;

  public Customer(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

  //Constructor to be used in tests only
  public Customer(Long id, String name, String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
  }
}
