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

  @Column(nullable = false)
  String street;

  @Column(nullable = false)
  Integer house_number;

  @Column(nullable = false)
  String city;

  @Column(nullable = false)
  String postalCode;
}
