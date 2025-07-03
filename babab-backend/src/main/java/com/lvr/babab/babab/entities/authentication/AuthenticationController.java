package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.entities.authentication.dto.*;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Routes.BASE_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticatedResponse> registerCustomer(
      @RequestBody @Valid RegisterUserRequest registerUserRequest) {
    AuthenticatedResponse authenticatedResponse =
        authenticationService.registerCustomerAccount(registerUserRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("{user/id}")
            .buildAndExpand(authenticatedResponse.user().id())
            .toUri();
    return ResponseEntity.created(location).body(authenticatedResponse);
  }

  @PostMapping("/register-business-account")
  public ResponseEntity<AuthenticatedResponse> registerBusinessAccount(
      @RequestBody @Valid RegisterBusinessAccountRequest request) {
    AuthenticatedResponse response = authenticationService.registerBusinessAccount(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("{user/id}")
            .buildAndExpand(response.user().id())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticatedResponse> login(@RequestBody LoginRequest loginRequest) {
    AuthenticatedResponse response = authenticationService.login(loginRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/password-reset/{id}")
  public ResponseEntity<Void> requestPasswordReset(@PathVariable Long id) {
    authenticationService.requestPasswordReset(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/password-new/{token}")
  public ResponseEntity<Void> resetPassword(
      @PathVariable String token, @Valid @RequestBody UserPasswordResetRequest requestBody) {
    authenticationService.resetPassword(token, requestBody);
    return ResponseEntity.ok().build();
  }
}
