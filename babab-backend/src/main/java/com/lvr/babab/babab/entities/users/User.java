package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.address.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, unique = true)
  String email;

  @Column(nullable = false)
  String password;

  @Column(nullable = true)
  String firstName;

  @Column(nullable = true)
  String lastName;

  @Column(nullable = true)
  LocalDate birthdate;

  @Column(nullable = false)
  LocalDate createdOn;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @JoinColumn(nullable = false, name = "address_id", unique = true)
  @OneToOne(cascade = CascadeType.ALL)
  Address address;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.toString()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
