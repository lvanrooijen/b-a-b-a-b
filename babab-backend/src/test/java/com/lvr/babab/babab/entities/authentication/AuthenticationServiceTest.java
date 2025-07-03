package com.lvr.babab.babab.entities.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.lvr.babab.babab.Mockdata.UserTestData;
import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.entities.authentication.dto.RegisterBusinessAccountRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterUserRequest;
import com.lvr.babab.babab.entities.email.MailService;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestRepository;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestService;
import com.lvr.babab.babab.entities.users.*;
import com.lvr.babab.babab.exceptions.users.DuplicateEmailException;
import com.lvr.babab.babab.exceptions.users.RegisterExistingAccountException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Test cases for methods in the AuthenticationService class. */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
  @Mock private PasswordResetRequestService passwordResetRequestService;
  @Mock private PasswordResetRequestRepository passwordResetRequestRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserRepository userRepository;
  @Mock private BusinessAccountRepository businessAccountRepository;
  @Mock private JwtService jwtService;
  @Mock private MailService mailService;

  @InjectMocks private AuthenticationService authenticationService;

  /** Test cases for the registerUserAccount method */
  @Nested
  class RegisterCustomerAccountRequestTest {
    @Test
    void register_customer_account_should_return_authenticated_response() {
      RegisterUserRequest registerUserRequest = UserTestData.getRegisterUserRequest();

      AuthenticatedResponse result =
          authenticationService.registerCustomerAccount(registerUserRequest);

      assertEquals(registerUserRequest.email(), result.user().email());
    }

    @Test
    void register_customer_account_should_call_repository_save_method() {
      RegisterUserRequest registerUserRequest = UserTestData.getRegisterUserRequest();

      authenticationService.registerCustomerAccount(registerUserRequest);

      verify(userRepository).save(any());
    }

    @Test
    void register_customer_account_should_throw_DuplicateEmailException_if_user_exists() {
      CustomerUser user = UserTestData.getCustomerUser();
      RegisterUserRequest registerUserRequest = UserTestData.getRegisterUserRequest();

      Mockito.when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      assertThrows(
          DuplicateEmailException.class,
          () -> authenticationService.registerCustomerAccount(registerUserRequest));
    }
  }

  /** Test cases for the registerUserAccount method */
  @Nested
  class RegisterBusinessUserAccountRequestTest {
    @Test
    void register_business_account_should_return_authenticated_response() {
      RegisterBusinessAccountRequest registerUserRequest =
          UserTestData.getRegisterBusinessAccountRequest();

      AuthenticatedResponse result =
          authenticationService.registerBusinessAccount(registerUserRequest);
      assertEquals(registerUserRequest.email(), result.user().email());
    }

    @Test
    void register_business_account_should_call_repository_save_method() {
      RegisterBusinessAccountRequest registerRequest =
          UserTestData.getRegisterBusinessAccountRequest();

      authenticationService.registerBusinessAccount(registerRequest);
      verify(userRepository).save(any());
    }

    @Test
    void register_business_account_should_throw_DuplicateEmailException_if_user_exists() {
      BusinessUser user = UserTestData.getBusinessUser();
      RegisterBusinessAccountRequest registerRequest =
          UserTestData.getRegisterBusinessAccountRequest();

      Mockito.when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      assertThrows(
          DuplicateEmailException.class,
          () -> authenticationService.registerBusinessAccount(registerRequest));
    }

    @Test
    void
        register_business_account_should_throw_RegisterExistingAccountException_if_kvk_number_exists() {
      BusinessUser user = UserTestData.getBusinessUser();
      RegisterBusinessAccountRequest registerRequest =
          UserTestData.getRegisterBusinessAccountRequest();

      Mockito.when(businessAccountRepository.findByKvkNumber(any())).thenReturn(Optional.of(user));

      assertThrows(
          RegisterExistingAccountException.class,
          () -> authenticationService.registerBusinessAccount(registerRequest));
    }

    @Test
    void
        register_business_account_should_throw_RegisterExistingAccountException_if_name_is_taken() {
      BusinessUser user = UserTestData.getBusinessUser();
      RegisterBusinessAccountRequest registerRequest =
          UserTestData.getRegisterBusinessAccountRequest();

      Mockito.when(businessAccountRepository.findByCompanyNameIgnoreCase(any()))
          .thenReturn(Optional.of(user));

      assertThrows(
          RegisterExistingAccountException.class,
          () -> authenticationService.registerBusinessAccount(registerRequest));
    }
  }

  /** Test cases for the login method */
  @Nested
  class LoginTest {
    @Test
    void login() {}
  }

  /** Test cases for the loadUserByUsername method */
  @Nested
  class LoadUserByUsernameTest {
    @Test
    void loadUserByUsername() {}
  }

  /** Test cases for the createUser method */
  @Nested
  class CreateUserTest {

    @Test
    void createUser() {}
  }

  /** Test cases for the updateUser method */
  @Nested
  class UpdateUserTest {

    @Test
    void updateUser() {}
  }

  /** Test cases for the DeleteUser method */
  @Nested
  class DeleteUserTest {

    @Test
    void deleteUser() {}
  }

  /** Test cases for the changePassword method */
  @Nested
  class ChangePasswordTest {

    @Test
    void changePassword() {}
  }

  /** Test cases for the UserExists method */
  @Nested
  class UserExistsTest {

    @Test
    void userExists() {}
  }

  /** Test cases for the RequestPasswordReset method */
  @Nested
  class RequestPasswordResetTest {

    @Test
    void requestPasswordReset() {}
  }

  /** Test cases for the ResetPassword method */
  @Nested
  class ResetPasswordTest {

    @Test
    void resetPassword() {}
  }
}
