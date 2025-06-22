package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.configurations.security.JwtToken;
import com.lvr.babab.babab.entities.authentication.dto.LoginRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterResponse;
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
  public ResponseEntity<RegisterResponse> register(
      @RequestBody @Valid RegisterRequest registerRequest) {
    RegisterResponse registerResponse = authenticationService.register(registerRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{/id}")
            .buildAndExpand(registerResponse.user().id())
            .toUri();
    return ResponseEntity.created(location).body(registerResponse);
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
}
