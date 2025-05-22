package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.configurations.security.JwtToken;
import com.lvr.babab.babab.entities.users.dto.*;
import com.lvr.babab.babab.exceptions.authentication.DuplicateEmailException;
import com.lvr.babab.babab.exceptions.authentication.FailedLoginException;
import com.lvr.babab.babab.exceptions.authentication.PasswordMismatchException;
import com.lvr.babab.babab.exceptions.authentication.UserNotFoundException;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsManager {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponseMax> getAll() {
    return userRepository.findAll().stream().map(UserResponseMax::to).toList();
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserResponseMax patchUser(Long id, PatchUser patch) {
    log.info("Patching user from service class with id {}", id);
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("[update user failed] reason=user id not found id=%s", id)));

    if (patch.firstname() != null) {
      user.setFirstName(patch.firstname());
    }

    if (patch.lastname() != null) {
      user.setLastName(patch.lastname());
    }

    if (patch.birthday() != null) {
      user.setBirthdate(patch.birthday());
    }

    if (patch.email() != null) {
      if (userRepository.findByEmailIgnoreCase(patch.email()).isPresent()) {
        throw new DuplicateEmailException(
            String.format(
                "[update user failed] reason=email already exists email=%s", patch.email()));
      }
    }

    userRepository.save(user);
    return UserResponseMax.to(user);
  }

  public RegisterResponse register(@Valid RegisterRequest registerRequest) {
    userRepository
        .findByEmailIgnoreCase(registerRequest.email())
        .orElseThrow(
            () ->
                new DuplicateEmailException(
                    String.format(
                        "[register failed] reason=email already registered, email=%s",
                        registerRequest.email())));

    User user =
        User.builder()
            .email(registerRequest.email())
            .password(passwordEncoder.encode(registerRequest.password()))
            .firstName(registerRequest.firstname())
            .lastName(registerRequest.lastname())
            .role(Role.USER)
            .createdOn(LocalDate.now())
            .build();

    createUser(user);

    UserResponseMin userResponseMin = new UserResponseMin(user.id, user.getEmail());
    String token = jwtService.generateTokenForUser(user);

    return new RegisterResponse(token, userResponseMin);
  }

  public JwtToken login(LoginRequest loginRequest) {
    User user =
        userRepository
            .findByEmailIgnoreCase(loginRequest.email())
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(
                            "[login failed] reason=user does not exist, email=%s",
                            loginRequest.email())));

    if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
      return new JwtToken(jwtService.generateTokenForUser(user));
    } else {
      throw new FailedLoginException(
          String.format(
              "[login failed] reason=invalid password : [email] %s", loginRequest.email()));
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmailIgnoreCase(username).orElse(null);
  }

  @Override
  public void createUser(UserDetails user) {
    userRepository.save((User) user);
  }

  @Override
  public void updateUser(UserDetails user) {
    userRepository.save((User) user);
  }

  @Override
  public void deleteUser(String username) {
    User user =
        userRepository
            .findByEmailIgnoreCase(username)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(
                            "[failed to delete user] reason=user not found email=%s", username)));
    userRepository.delete(user);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      User user =
          userRepository
              .findByEmailIgnoreCase(authentication.getName())
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException(
                          String.format(
                              "[failed to change password] reason=user not found email=%s",
                              authentication.getName())));
      if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
        throw new PasswordMismatchException(
            String.format(
                "[failed to change password] reason=invalid password email=%s",
                user.getUsername()));
      } else {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
      }
    }
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.findByEmailIgnoreCase(username).isPresent();
  }
}
