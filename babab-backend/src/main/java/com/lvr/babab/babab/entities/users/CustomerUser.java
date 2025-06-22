package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.*;

@Table(name="customer_users")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class CustomerUser extends User {
  @Column(name = "first_name", nullable = false, length = 50)
  String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  String lastName;

  @Column(name = "birthdate", nullable = false)
  LocalDate birthdate;

  @Builder
  public CustomerUser(Long id, String email, String password, LocalDate createdOn, Role role, Address address, String firstName, String lastName, LocalDate birthdate) {
    super(id, email, password, createdOn, role, address);
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
  }

  public CustomerUser(String firstName, String lastName, LocalDate birthdate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
  }
}
