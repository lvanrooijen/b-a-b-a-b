package com.lvr.babab.babab.entities.passwordreset;

import com.lvr.babab.babab.entities.users.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "password_reset_requests")
@Entity
@NoArgsConstructor
@Getter
public class PasswordResetRequest {

  @Builder
  public PasswordResetRequest(UUID verificationCode, User user) {
    this.verificationCode = verificationCode;
    this.user = user;
    this.issuedAt = LocalDateTime.now();
    this.isExpired = isExpired();
  }

  @Id @GeneratedValue Long id;

  @Column(nullable = false)
  UUID verificationCode;

  @Column(nullable = false)
  LocalDateTime issuedAt;

  @ManyToOne(optional = false)
  User user;

  @Column(nullable = false)
  boolean isExpired;

  public Boolean isExpired() {
    return LocalDateTime.now().isAfter(issuedAt.minusMinutes(30));
  }
}
