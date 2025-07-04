package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.entities.authentication.dto.*;
import com.lvr.babab.babab.entities.email.MailService;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequest;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestRepository;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestService;
import com.lvr.babab.babab.entities.users.*;
import com.lvr.babab.babab.exceptions.authentication.*;
import com.lvr.babab.babab.exceptions.users.DuplicateEmailException;
import com.lvr.babab.babab.exceptions.users.RegisterExistingAccountException;
import com.lvr.babab.babab.exceptions.users.UserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsManager {
  private final PasswordResetRequestService passwordResetRequestService;
  private final PasswordResetRequestRepository passwordResetRequestRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final BusinessAccountRepository businessAccountRepository;
  private final JwtService jwtService;
  private final MailService mailService;

  public AuthenticatedResponse registerCustomerAccount(@Valid RegisterUserRequest requestBody) {
    if (userExists(requestBody.email())) {
      throw new DuplicateEmailException(
          String.format(
              "[register failed] reason=email already registered, email=%s", requestBody.email()));
    }

    User user =
        CustomerUser.builder()
            .email(requestBody.email())
            .password(passwordEncoder.encode(requestBody.password()))
            .firstName(requestBody.firstname())
            .lastName(requestBody.lastname())
            .birthdate(requestBody.birthdate())
            .role(Role.USER)
            .createdOn(LocalDate.now())
            .build();

    createUser(user);

    BasicUserResponse basicUserResponse = new BasicUserResponse(user.getId(), user.getEmail());
    String token = jwtService.generateTokenForUser(user);

    return new AuthenticatedResponse(token, basicUserResponse);
  }

  public AuthenticatedResponse registerBusinessAccount(RegisterBusinessAccountRequest requestBody) {
    if (userExists(requestBody.email())) {
      throw new DuplicateEmailException(
          String.format(
              "[register failed] reason=email already registered, email=%s", requestBody.email()));
    }

    if (businessAccountRepository.findByKvkNumber(requestBody.kvkNumber()).isPresent()) {
      throw new RegisterExistingAccountException(
          String.format(
              "[register failed] reason=KVK number is already registered, kvkNumber=%s",
              requestBody.kvkNumber()),
          String.format(
              "A company with KVK number %s was already registered ", requestBody.kvkNumber()));
    }

    if (businessAccountRepository
        .findByCompanyNameIgnoreCase(requestBody.companyName())
        .isPresent()) {
      throw new RegisterExistingAccountException(
          String.format(
              "[register failed] reason=Company name is already registered, companyName=%s",
              requestBody.companyName()),
          String.format(
              "A Company by the name of %s was already registered", requestBody.companyName()));
    }

    User user =
        BusinessUser.builder()
            .email(requestBody.email())
            .password(passwordEncoder.encode(requestBody.password()))
            .companyName(requestBody.companyName())
            .kvkNumber(requestBody.kvkNumber())
            .role(Role.USER)
            .createdOn(LocalDate.now())
            .build();

    createUser(user);

    BasicUserResponse basicUserResponse = new BasicUserResponse(user.getId(), user.getEmail());
    String token = jwtService.generateTokenForUser(user);

    return new AuthenticatedResponse(token, basicUserResponse);
  }

  public AuthenticatedResponse login(LoginRequest requestBody) {
    User user =
        userRepository
            .findByEmailIgnoreCase(requestBody.email())
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(
                            "[login failed] reason=user does not exist, email=%s",
                            requestBody.email())));

    if (passwordEncoder.matches(requestBody.password(), user.getPassword())) {
      return new AuthenticatedResponse(
          jwtService.generateTokenForUser(user),
          new BasicUserResponse(user.getId(), user.getEmail()));
    } else {
      throw new FailedLoginException(
          String.format(
              "[login failed] reason=invalid password : [email] %s", requestBody.email()));
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

  /**
   * Method to change a users password, not suitable for forgotten password scenario's
   *
   * @param oldPassword
   * @param newPassword
   */
  @Override
  public void changePassword(String oldPassword, String newPassword) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      User user =
          userRepository
              .findByEmailIgnoreCase(authentication.getName())
              .orElseThrow(
                  () ->
                      new UserNotFoundException(
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

  @Transactional
  public void requestPasswordReset(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format(
                            "[failed to send password reset email] reason=user not found id=%s",
                            id)));

    PasswordResetRequest resetRequest = passwordResetRequestService.requestPasswordReset(user);

    String resetUrl = generateResetUrl(resetRequest.getVerificationCode());

    try {

      String name =
          user instanceof CustomerUser
              ? ((CustomerUser) user).getFirstName()
              : ((BusinessUser) user).getCompanyName();

      mailService.sendPasswordResetEmail(user.getEmail(), name, resetUrl);
    } catch (MessagingException e) {
      throw new FailedSendPasswordResetEmailException(
          String.format(
              "[failed to send password reset email] reason=[MessagingException] email=%s",
              user.getEmail()));
    }
  }

  private String generateResetUrl(UUID resetCode) {
    return String.format("https://www.babab.com/password-new/%s", resetCode);
  }

  public void resetPassword(String token, UserPasswordResetRequest requestBody) {
    User user =
        userRepository
            .findByEmailIgnoreCase(requestBody.email())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format(
                            "[failed to update password] reason=user by this email is can not be found email=[%s]",
                            requestBody.email())));

    PasswordResetRequest resetRequest =
        passwordResetRequestRepository
            .findByUser(user)
            .orElseThrow(
                () ->
                    new PasswordRequestNotFound(
                        String.format("[failed to update password] reason=[invalid reset token]")));

    if (!token.equals(resetRequest.getVerificationCode().toString())) {
      throw new ForbiddenException("[failed to reset password] reason=invalid reset code]");
    }

    user.setPassword(passwordEncoder.encode(requestBody.password()));
    userRepository.save(user);
    passwordResetRequestRepository.delete(resetRequest);
  }
}
