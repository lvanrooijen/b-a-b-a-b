package com.lvr.babab.babab.entities.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.lvr.babab.babab.Mockdata.UserTestData;
import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.entities.authentication.dto.LoginRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterBusinessAccountRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterUserRequest;
import com.lvr.babab.babab.entities.email.MailService;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestRepository;
import com.lvr.babab.babab.entities.passwordreset.PasswordResetRequestService;
import com.lvr.babab.babab.entities.users.*;
import com.lvr.babab.babab.exceptions.authentication.FailedLoginException;
import com.lvr.babab.babab.exceptions.authentication.PasswordMismatchException;
import com.lvr.babab.babab.exceptions.users.DuplicateEmailException;
import com.lvr.babab.babab.exceptions.users.RegisterExistingAccountException;
import com.lvr.babab.babab.exceptions.users.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    void login_should_return_authenticated_response() {
      LoginRequest loginRequest = UserTestData.getLoginRequest();
      CustomerUser customerUser = UserTestData.getCustomerUser();

      when(userRepository.findByEmailIgnoreCase(loginRequest.email()))
          .thenReturn(Optional.of(customerUser));

      when(passwordEncoder.matches(any(), any())).thenReturn(true);

      AuthenticatedResponse result = authenticationService.login(loginRequest);
      assertEquals(loginRequest.email(), result.user().email());
    }

    @Test
    void login_should_return_jwt_token() {
      LoginRequest loginRequest = UserTestData.getLoginRequest();
      CustomerUser customerUser = UserTestData.getCustomerUser();

      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(customerUser));
      when(passwordEncoder.matches(any(), any())).thenReturn(true);
      when(jwtService.generateTokenForUser(any())).thenReturn("token");
      AuthenticatedResponse result = authenticationService.login(loginRequest);
      assertNotNull("token", result.token());
    }

    @Test
    void login_should_throw_UserNotFoundException_if_email_does_not_exist() {
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

      assertThrows(
          UserNotFoundException.class,
          () -> authenticationService.login(UserTestData.getLoginRequest()));
    }

    @Test
    void login_invalid_password_should_throw_FailedLoginException() {
      when(userRepository.findByEmailIgnoreCase(any()))
          .thenReturn(Optional.of(UserTestData.getCustomerUser()));
      when(passwordEncoder.matches(any(), any())).thenReturn(false);

      assertThrows(
          FailedLoginException.class,
          () -> authenticationService.login(UserTestData.getLoginRequest()));
    }
  }

  /** Test cases for the loadUserByUsername method */
  @Nested
  class LoadUserByUsernameTest {
    @Test
    void load_user_by_username_should_return_user() {
      User user = UserTestData.getCustomerUser();

      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      User result = (User) authenticationService.loadUserByUsername(user.getEmail());
      assertNotNull(result);
      assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void load_user_by_username_when_user_not_found_should_return_null() {
      User user = UserTestData.getCustomerUser();

      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

      User result = (User) authenticationService.loadUserByUsername(user.getEmail());
      assertNull(result);
    }
  }

  /** Test cases for the createUser method */
  @Nested
  class CreateUserTest {

    @Test
    void create_user_calls_repository_save_method() {
      User user = UserTestData.getCustomerUser();

      authenticationService.createUser(user);

      verify(userRepository).save(any());
    }
  }

  /** Test cases for the updateUser method */
  @Nested
  class UpdateUserTest {

    @Test
    void update_user_call_repository_save_method() {
      User user = UserTestData.getCustomerUser();

      authenticationService.updateUser(user);

      verify(userRepository).save(any());
    }
  }

  /** Test cases for the DeleteUser method */
  @Nested
  class DeleteUserTest {

    @Test
    void delete_user_calls_repository_delete_method() {
      User user = UserTestData.getCustomerUser();

      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      authenticationService.deleteUser(user.getEmail());

      verify(userRepository).delete(any());
    }

    @Test
    void delete_user_non_existent_user_calls_throws_UserNotFoundException() {
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

      assertThrows(
          UserNotFoundException.class, () -> authenticationService.deleteUser("any@email.com"));
    }
  }

  /** Test cases for the changePassword method */
  @Nested
  class ChangePasswordTest {

    @BeforeEach
    void setUp() {
      User user = UserTestData.getCustomerUser();
      Authentication mockAuth = mock(Authentication.class);
      when(mockAuth.getName()).thenReturn(user.getEmail());

      SecurityContext mockContext = mock(SecurityContext.class);
      when(mockContext.getAuthentication()).thenReturn(mockAuth);

      SecurityContextHolder.setContext(mockContext);
    }

    @Test
    void change_password_should_call_repository_save_method() {
      User user = UserTestData.getCustomerUser();
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      when(passwordEncoder.matches(any(), any())).thenReturn(true);

      authenticationService.changePassword("Password123!", "Password123!");
      verify(userRepository).save(any());
    }

    @Test
    void change_password_should_call_encode_password_method() {
      User user = UserTestData.getCustomerUser();
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      when(passwordEncoder.matches(any(), any())).thenReturn(true);

      authenticationService.changePassword("Password123!", "Password123!");
      verify(passwordEncoder).encode(any());
    }

    @Test
    void change_password_user_not_found_should_throw_UserNotFoundException() {
      User user = UserTestData.getCustomerUser();
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

      assertThrows(
          UserNotFoundException.class,
          () -> authenticationService.changePassword("Password123!", "Password123!"));
    }

    @Test
    void change_password_with_different_passwords_should_throw_PasswordMismatchException() {
      User user = UserTestData.getCustomerUser();
      when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));

      assertThrows(
          PasswordMismatchException.class,
          () -> authenticationService.changePassword("Password123!", "Mismatch!!123!"));
    }
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
