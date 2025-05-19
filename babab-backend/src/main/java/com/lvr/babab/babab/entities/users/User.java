package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.address.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetailsManager {
  // TODO add createdOn date, and a birthdate!
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, unique = true)
  String username;

  @Column(nullable = false)
  String password;

  @Column(nullable = false, unique = true)
  String email;

  @Column(nullable = true)
  String firstName;

  @Column(nullable = true)
  String lastName;

  @Column(nullable = true)
  LocalDate dateOfBirth;

  @Column(nullable = false)
  LocalDate createdDate;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @JoinColumn(nullable = false, name = "address_id", unique = true)
  @OneToOne(cascade = CascadeType.ALL)
  Address address;

  @Override
  public void createUser(UserDetails user) {
    // TODO
  }

  @Override
  public void updateUser(UserDetails user) {
    // TODO
  }

  @Override
  public void deleteUser(String username) {
    // TODO
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    // TODO
  }

  @Override
  public boolean userExists(String username) {
    // TODO
    return false;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO
    return null;
  }
}
