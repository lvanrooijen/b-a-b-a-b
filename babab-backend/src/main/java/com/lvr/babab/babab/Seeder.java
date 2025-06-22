package com.lvr.babab.babab;

import com.lvr.babab.babab.entities.users.AdminUser;
import com.lvr.babab.babab.entities.users.Role;
import com.lvr.babab.babab.entities.users.User;
import com.lvr.babab.babab.entities.users.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Seeder implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    seedAdmin();
  }

  private void seedAdmin() {
    if (userRepository.findByEmailIgnoreCase("admin@admin.com").isPresent()) return;

    User admin =
        AdminUser.builder()
            .email("admin@admin.com")
            .password(passwordEncoder.encode("securePassword1234!"))
            .createdOn(LocalDate.now())
            .role(Role.ADMIN)
            .failedLoginAttempts(0)
            .lastLogin(LocalDateTime.now())
            .build();
    userRepository.save(admin);
  }
}
