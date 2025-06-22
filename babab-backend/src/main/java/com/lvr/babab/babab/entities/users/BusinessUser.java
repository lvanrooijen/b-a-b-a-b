package com.lvr.babab.babab.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Table(name = "business_users")
@Entity
@Getter
@Setter
public class BusinessUser extends User {
  @Column(name = "company_name", length = 250)
  private String companyName;

  @Pattern(regexp = "^\\d{8}$", message = "KvK-number must be 8 characters")
  @Column(name = "kvk_number", length = 8, nullable = false)
  private String kvkNumber;
}
