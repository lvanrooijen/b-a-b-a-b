package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.configurations.security.JwtToken;
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
  public ResponseEntity<RegisterUserResponse> register(
      @RequestBody @Valid RegisterUserRequest registerUserRequest) {
    RegisterUserResponse registerUserResponse =
        authenticationService.registerUserAccount(registerUserRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{/id}")
            .buildAndExpand(registerUserResponse.user().id())
            .toUri();
    return ResponseEntity.created(location).body(registerUserResponse);
  }

  @PostMapping("/register-business-account")
  public ResponseEntity<RegisterUserResponse> registerBusinessAccount(
      @RequestBody @Valid RegisterBusinessAccountRequest request) {
    RegisterUserResponse response = authenticationService.registerBusinessAccount(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{/id}")
            .buildAndExpand(response.user().id())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {
    JwtToken token = authenticationService.login(loginRequest);
    return ResponseEntity.ok(token);
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
