package com.lvr.babab.babab.entities.authentication;

import com.lvr.babab.babab.configurations.JwtService;
import com.lvr.babab.babab.configurations.JwtToken;
import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.entities.authentication.dto.LoginRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterResponse;
import com.lvr.babab.babab.entities.users.UserService;
import com.lvr.babab.babab.entities.users.dto.UserResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.AUTH)
public class AuthenticationController {
  private final UserService userService;
  private final JwtService jwtService;

  // user registreren
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
      @RequestBody @Valid RegisterRequest registerRequest) {
    RegisterResponse registerResponse = userService.register(registerRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("{/id}")
            .buildAndExpand(registerResponse.user().id())
            .toUri();
    return ResponseEntity.created(location).body(registerResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {

    JwtToken token = userService.login(loginRequest);

    return ResponseEntity.ok(token);
  }

  // @PatchMapping("/edit")
  // public ResponseEntity<UserResponse> edit(@RequestBody PatchUser patch) {}

  // user updaten

  // user inloggen

  // user ww veranderen

  // user eigen account verwijderen
}
