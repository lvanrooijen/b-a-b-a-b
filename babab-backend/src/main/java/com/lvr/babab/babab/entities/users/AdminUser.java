package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "admin_users")
@Getter
@Setter
@NoArgsConstructor
public class AdminUser extends User {
  @Column(name = "failed_login_attempts", nullable = false)
  private int failedLoginAttempts;

  @Column(name = "last_login", nullable = false)
  private LocalDateTime lastLogin;

  @Builder
  public AdminUser(
      Long id,
      String email,
      String password,
      LocalDate createdOn,
      Role role,
      Address address,
      int failedLoginAttempts,
      LocalDateTime lastLogin) {
    super(id, email, password, createdOn, role, address);
    this.failedLoginAttempts = failedLoginAttempts;
    this.lastLogin = lastLogin;
  }

  public AdminUser(int failedLoginAttempts, LocalDateTime lastLogin) {
    this.failedLoginAttempts = failedLoginAttempts;
    this.lastLogin = lastLogin;
  }
}
