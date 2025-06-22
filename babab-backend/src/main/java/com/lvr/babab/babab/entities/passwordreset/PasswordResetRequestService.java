package com.lvr.babab.babab.entities.passwordreset;

import com.lvr.babab.babab.entities.users.User;
import com.lvr.babab.babab.exceptions.authentication.PendingPasswordResetRequestException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetRequestService {
  private final PasswordResetRequestRepository passwordResetRequestRepository;

  public PasswordResetRequest requestPasswordReset(User user) {
    PasswordResetRequest resetRequest =
        passwordResetRequestRepository
            .findByUser(user)
            .orElse(
                PasswordResetRequest.builder()
                    .verificationCode(UUID.randomUUID())
                    .user(user)
                    .build());

    if (resetRequest.isExpired()) {
      passwordResetRequestRepository.delete(resetRequest);
      resetRequest =
          PasswordResetRequest.builder().verificationCode(UUID.randomUUID()).user(user).build();
    } else if (!resetRequest.isExpired()) {
      Duration duration = Duration.between(resetRequest.issuedAt, LocalDateTime.now());
      throw new PendingPasswordResetRequestException(
          String.format(
              "[failed to request password reset] reason=previous password request not expired yet, expiresIn=%s minutes",
              duration),
          duration);
    }

    return passwordResetRequestRepository.save(resetRequest);
  }
}
