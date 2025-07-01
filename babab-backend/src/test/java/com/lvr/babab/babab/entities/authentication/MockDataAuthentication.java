package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.entities.authentication.dto.AuthenticatedResponse;
import com.lvr.babab.babab.entities.authentication.dto.BasicUserResponse;
import com.lvr.babab.babab.entities.authentication.dto.RegisterUserRequest;
import java.time.LocalDate;

public class MockDataAuthentication {
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

  public static AuthenticatedResponse getGetUserMockedResponse() {
    return new AuthenticatedResponse(
        "mocked-jwt-token", new BasicUserResponse(1L, "Calvin_Cordozar_Broadus_Jr@mail.com"));
  }

  public static RegisterUserRequest getUserRequestCustom(String type, String value) {
    if (type.equals("email")) return getUserRequestCustomEmail(value);
    if (type.equals("password")) return getUserRequestCustomPassword(value);
    if (type.equals("firstname")) return getUserRequestCustomFirstname(value);
    if (type.equals("lastname")) return getUserRequestCustomLastname(value);

    throw new InvalidSelectionException(
        type
            + " is not a valid type. You can use : email, password, firstname, lastname or birthdate. birthdate must be a LocalDate");
  }

  public static RegisterUserRequest getUserRequestCustom(String type, LocalDate birthdate) {
    if (type.equals("birthdate")) return getUserRequestCustomBirthdate(birthdate);

    throw new InvalidSelectionException(
        type
            + " is not a valid type.  Options: email, password, firstname, lastname or birthdate. birthdate must be a LocalDate");
  }

  public static RegisterUserRequest getUserRequestCustomEmail(String email) {
    return new RegisterUserRequest(
        email, "Password123!", "snoop", "dogg", LocalDate.of(1980, 1, 1));
  }

  public static RegisterUserRequest getUserRequestCustomPassword(String password) {
    return new RegisterUserRequest(
        "Calvin_Cordozar_Broadus_Jr@mail.com", password, "snoop", "dogg", LocalDate.of(1980, 1, 1));
  }

  public static RegisterUserRequest getUserRequestCustomFirstname(String firstname) {
    return new RegisterUserRequest(
        "Calvin_Cordozar_Broadus_Jr@mail.com",
        "Password123!",
        firstname,
        "dogg",
        LocalDate.of(1980, 1, 1));
  }

  public static RegisterUserRequest getUserRequestCustomLastname(String lastname) {
    return new RegisterUserRequest(
        "Calvin_Cordozar_Broadus_Jr@mail.com",
        "Password123!",
        "snoop",
        lastname,
        LocalDate.of(1980, 1, 1));
  }

  public static RegisterUserRequest getUserRequestCustomBirthdate(LocalDate birthdate) {
    return new RegisterUserRequest(
        "Calvin_Cordozar_Broadus_Jr@mail.com", "Password123!", "snoop", "dogg", birthdate);
  }
}
