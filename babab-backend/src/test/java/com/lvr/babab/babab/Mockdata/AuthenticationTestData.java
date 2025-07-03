package com.lvr.babab.babab.Mockdata;

import com.lvr.babab.babab.entities.authentication.InvalidSelectionException;
import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.entities.authentication.dto.BasicUserResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class AuthenticationTestData {
  public static String getUserPayloadValid() {
    return """
      {
        "email":"Calvin_Cordozar_Broadus_Jr@email.nl",
        "password":"Password123!",
        "firstname":"snoop",
        "lastname":"dogg",
        "birthdate": "1980-01-10"
      }
      """;
  }

  public static String getUserPayloadCustom(String type, String value) {
    String email = type.equals("email") ? value : "defaultEmail@example.com";
    String password = type.equals("password") ? value : "DefaultPassword123!";
    String firstName = type.equals("firstname") ? value : "defaultFirstName";
    String lastName = type.equals("lastname") ? value : "defaultLastName";
    String birthDate = type.equals("birthdate") ? value : "2000-01-01";

    return """
        {
            "email":"%s",
            "password":"%s",
            "firstname":"%s",
            "lastname":"%s",
            "birthdate":"%s"
        }
        """
        .formatted(email, password, firstName, lastName, birthDate);
  }

  public static String getUserBusinessAccountPayloadCustom(String type, String value) {
    ArrayList<String> availableTypes =
        new ArrayList<>(Arrays.asList("email", "password", "companyName", "kvkNumber"));

    if (!availableTypes.contains(type)) {
      throw new InvalidSelectionException("Invalid type of payload");
    }

    String companyName = type.equals("companyName") ? value : "Green Cookie Monster inc. 1988";
    String kvkNumber = type.equals("kvkNumber") ? value : "12345678";
    String email = type.equals("email") ? value : "greencookie@monsterinc.com";
    String password = type.equals("password") ? value : "Password123!";
    return """
           {
              "companyName":"%s",
              "kvkNumber":"%s",
              "email":"%s",
              "password":"%s"
           }
           """
        .formatted(companyName, kvkNumber, email, password);
  }

  public static String getUserBusinessAccountPayloadValid() {
    return """
           {
              "companyName":"Green Cookie Monster inc. 1988",
              "kvkNumber":"12345678",
              "email":"greencookie@monsterinc.com",
              "password":"Password123!"
           }
           """;
  }

  public static String getUserLoginPayloadValid() {
    return """
           {
              "email":"greencookie@monsterinc.com",
              "password":"Password123!"
           }
           """;
  }

  public static String getPasswordChangePayloadValid() {
    return """
           {
               "email":"test@email.com",
               "password":"SecurePassword123!",
               "confirmPassword":"SecurePassword123!"
           }
           """;
  }

  public static AuthenticatedResponse getGetUserMockedResponse() {
    return new AuthenticatedResponse(
        "mocked-jwt-token", new BasicUserResponse(1L, "default@email.com"));
  }

  public static AuthenticatedResponse getGetUserMockedResponse(String email) {
    return new AuthenticatedResponse("mocked-jwt-token", new BasicUserResponse(1L, email));
  }

  public static AuthenticatedResponse getGetUserMockedResponse(Long id) {
    return new AuthenticatedResponse(
        "mocked-jwt-token", new BasicUserResponse(id, "default@email.com"));
  }

  public static AuthenticatedResponse getGetUserMockedResponse(Long id, String email) {
    return new AuthenticatedResponse("mocked-jwt-token", new BasicUserResponse(id, email));
  }

  public static AuthenticatedResponse getUserBusinessAccountMockedResponse() {
    return null;
    // "Green Cookie Monster inc. 1988","12345678","greencookie@monsterinc.com"
  }
}
