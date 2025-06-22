package com.lvr.babab.babab.entities.address;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @Column(name = "street", nullable = false)
  String street;

  @Column(name = "house_number", nullable = false)
  Integer house_number;

  @Column(name = "city", nullable = false)
  String city;

  @Column(name = "postal_code", nullable = false)
  String postalCode;
}
