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
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "email", nullable = false, unique = true)
  String email;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "created_on", nullable = false)
  LocalDate createdOn;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @JoinColumn(name = "address_id", unique = true)
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  Address address;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.toString()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  public Boolean isAdmin() {
    return role.equals(Role.ADMIN);
  }
}
