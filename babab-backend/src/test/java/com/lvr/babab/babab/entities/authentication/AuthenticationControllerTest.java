package com.lvr.babab.babab.entities.authentication;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lvr.babab.babab.configurations.security.CorsConfig;
import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.configurations.security.SecurityConfig;
import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.util.TestUtilities;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthenticationController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({SecurityConfig.class, CorsConfig.class})
class AuthenticationControllerTest {
  private final String baseEndpoint = "/api/v1/";
  private final String registerEndpoint = baseEndpoint + "register";
  private final String registerBusinessAccountEndpoint = baseEndpoint + "register-business-account";

  @Autowired MockMvc mockMvc;
  @MockitoBean AuthenticationService authenticationService;

  @MockitoBean JwtService jwtService;

  @Test
  void register_should_return_created() throws Exception {
    AuthenticatedResponse mockResponse =
        MockDataAuthentication.getGetUserMockedResponse("Calvin_Cordozar_Broadus_Jr@mail.com");

    Mockito.when(authenticationService.registerUserAccount(Mockito.any())).thenReturn(mockResponse);

    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadValid()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
  }

  @Test
  void register_user_password_without_a_number_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "Password!")))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.password").value("Password must contain at least 1 digit"));
  }

  @Test
  void register_user_password_without_a_capital_case_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "password123!"))
                .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.password")
                .value("Password must contain at least 1 uppercase letter"));
  }

  @Test
  void register_user_password_without_a_lowercase_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "PASSWORD123!"))
                .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.password")
                .value("Password must contain at least 1 lowercase letter"));
  }

  @Test
  void register_user_password_without_a_special_characters_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "Password123")))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.password")
                .value("Password must contain at least 1 special character"));
  }

  @Test
  void register_user_password_contains_blank_space_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "Password 123")))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.password").value("Password may not contain blank spaces"));
  }

  @Test
  void register_user_password_contains_less_then_8_characters_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("password", "Passw")))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.password")
                .value("Password length should be between 8 and 30 characters"));
  }

  @Test
  void register_user_password_contains_more_then_30_characters_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserPayloadCustom(
                        "password",
                        "Password1231231231231231231231231231231231231231231231231231231231231231231231231231231231231231231231231231231232131231231231231231!")))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.password")
                .value("Password length should be between 8 and 30 characters"));
  }

  @Test
  void register_user_with_invalid_email_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("email", "badEmailFormat")))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.email").value("Invalid email address"));
  }

  @Test
  void register_user_older_then_150_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom("birthdate", "1000-01-01")))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.birthdate").value("Oke Vlad The Impaler, you're a +150 years old"));
  }

  @Test
  void register_user_younger_then_18_should_return_bad_request() throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserPayloadCustom(
                        "birthdate", LocalDate.now().toString())))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.birthdate").value("Minimum age of 18 required"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"password", "email", "birthdate", "firstname", "lastname"})
  public void register_user_with_empty_fields_should_return_bad_request(String type)
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom(type, "")))
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {"firstname", "lastname"})
  public void register_user_with_blank_space_as_name_should_return_bad_request(String type)
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserPayloadCustom(type, " ")))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_firstname_longer_then_50_characters_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserPayloadCustom(
                        "firstname", TestUtilities.getRandomString(51))))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.firstname").value("First name must be between 3 and 50 characters"));
  }

  @Test
  public void register_user_with_lastname_longer_then_100_characters_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserPayloadCustom(
                        "lastname", TestUtilities.getRandomString(101))))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.lastname").value("Last name must be between 3 and 100 characters"));
  }

  @Test
  void register_business_account_should_return_created() throws Exception {
    AuthenticatedResponse mockResponse =
        MockDataAuthentication.getGetUserMockedResponse("greencookie@monsterinc.com");

    Mockito.when(authenticationService.registerBusinessAccount(Mockito.any()))
        .thenReturn(mockResponse);

    mockMvc
        .perform(
            post(registerBusinessAccountEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MockDataAuthentication.getUserBusinessAccountPayloadValid()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
  }

  @Test
  void register_business_account_company_name_too_short_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerBusinessAccountEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserBusinessAccountPayloadCustom("companyName", "a")))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.companyName")
                .value("Company name must be between 3 and 250 characters"));
  }

  @Test
  void register_business_account_company_name_too_long_should_return_bad_request()
      throws Exception {
    mockMvc
        .perform(
            post(registerBusinessAccountEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserBusinessAccountPayloadCustom(
                        "companyName", TestUtilities.getRandomString(251))))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.companyName")
                .value("Company name must be between 3 and 250 characters"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"12345678910", "123", "ABcd5678", " 2345678", "-12345678", "1234567!"})
  void register_business_account_invalid_kvk_number_should_return_bad_request(String kvkNumber)
      throws Exception {
    mockMvc
        .perform(
            post(registerBusinessAccountEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MockDataAuthentication.getUserBusinessAccountPayloadCustom(
                        "kvkNumber", kvkNumber)))
        .andExpect(jsonPath("$.detail").value("Invalid Request Body"));
  }

  @Test
  void login() {}

  @Test
  void requestPasswordReset() {}

  @Test
  void resetPassword() {}
}
