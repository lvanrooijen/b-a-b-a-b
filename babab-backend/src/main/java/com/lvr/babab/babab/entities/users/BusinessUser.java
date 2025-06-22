package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "business_users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BusinessUser extends User {
  @Column(name = "company_name", length = 250)
  private String companyName;

  @Pattern(regexp = "^\\d{8}$", message = "KvK-number must be 8 characters")
  @Column(name = "kvk_number", length = 8, nullable = false)
  private String kvkNumber;

  @Builder
  public BusinessUser(
      Long id,
      String email,
      String password,
      LocalDate createdOn,
      Role role,
      Address address,
      String companyName,
      String kvkNumber) {
    super(id, email, password, createdOn, role, address);
    this.companyName = companyName;
    this.kvkNumber = kvkNumber;
  }

  public BusinessUser(String companyName, String kvkNumber) {
    this.companyName = companyName;
    this.kvkNumber = kvkNumber;
  }
}
