package com.lvr.babab.babab.Mockdata;

import com.lvr.babab.babab.entities.address.Address;
import com.lvr.babab.babab.entities.authentication.dto.RegisterBusinessAccountRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterUserRequest;
import com.lvr.babab.babab.entities.users.BusinessUser;
import com.lvr.babab.babab.entities.users.CustomerUser;
import com.lvr.babab.babab.entities.users.Role;
import java.time.LocalDate;

public class UserTestData {
  public static String COMPANY_NAME = "bedrijf inc.";
  public static String KVK_NUMBER = "12345678";
  public static String EMAIL = "test@email.com";
  public static String PASSWORD = "password";
  public static String FIRST_NAME = "testFirstName";
  public static String LAST_NAME = "testLastName";
  public static Role ROLE = Role.USER;
  public static Long ID = 1L;
  public static LocalDate CREATED_ON = LocalDate.now();
  public static LocalDate BIRTHDATE = LocalDate.of(1981, 1, 1);

  public static CustomerUser getCustomerUser() {
    Address address = new Address();
    return new CustomerUser(
        ID, EMAIL, PASSWORD, CREATED_ON, ROLE, address, FIRST_NAME, LAST_NAME, BIRTHDATE);
  }

  public static BusinessUser getBusinessUser() {
    return new BusinessUser(COMPANY_NAME, KVK_NUMBER);
  }

  public static RegisterUserRequest getRegisterUserRequest() {
    return new RegisterUserRequest(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, BIRTHDATE);
  }

  public static RegisterBusinessAccountRequest getRegisterBusinessAccountRequest() {
    return new RegisterBusinessAccountRequest(COMPANY_NAME, KVK_NUMBER, EMAIL, PASSWORD);
  }
}
