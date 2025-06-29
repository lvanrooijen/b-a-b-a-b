package com.lvr.babab.babab.entities.authentication;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lvr.babab.babab.configurations.security.CorsConfig;
import com.lvr.babab.babab.configurations.security.JwtService;
import com.lvr.babab.babab.configurations.security.SecurityConfig;
import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.entities.authentication.dto.BasicUserResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
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
  @Autowired MockMvc mockMvc;
  @MockitoBean AuthenticationService authenticationService;

  @MockitoBean JwtService jwtService;

  @Test
  void register_should_return_created() throws Exception {
    AuthenticatedResponse mockResponse =
        new AuthenticatedResponse(
            "mocked-jwt-token", new BasicUserResponse(1L, "Calvin_Cordozar_Broadus_Jr@mail.com"));

    Mockito.when(authenticationService.registerUserAccount(Mockito.any())).thenReturn(mockResponse);

    mockMvc
        .perform(
            post("/api/v1/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email":"Calvin_Cordozar_Broadus_Jr@email.nl",
                        "password":"Password123!",
                        "firstname":"snoop",
                        "lastname":"dogg",
                        "birthdate": "1980-01-10"
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
  }

  @Test
  void register_user_older_then_150_should_return_created_bad_request() throws Exception {
    AuthenticatedResponse mockResponse =
        new AuthenticatedResponse(
            "mocked-jwt-token", new BasicUserResponse(1L, "Calvin_Cordozar_Broadus_Jr@mail.com"));

    Mockito.when(authenticationService.registerUserAccount(Mockito.any())).thenReturn(mockResponse);

    mockMvc
        .perform(
            post("/api/v1/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                    {
                                        "email":"Calvin_Cordozar_Broadus_Jr.@email.nl",
                                        "password":"Password123!",
                                        "firstname":"snoop",
                                        "lastname":"dogg",
                                        "birthdate": "1001-01-10"
                                    }
                                    """))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.birthdate").value("Oke Vlad The Impaler, you're a +150 years old"));
  }

  @Test
  void registerBusinessAccount() {}

  @Test
  void login() {}

  @Test
  void requestPasswordReset() {}

  @Test
  void resetPassword() {}
}
