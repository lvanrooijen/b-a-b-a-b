package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.configurations.security.JwtToken;
import com.lvr.babab.babab.entities.users.dto.LoginRequest;
import com.lvr.babab.babab.entities.users.dto.RegisterRequest;
import com.lvr.babab.babab.entities.users.dto.RegisterResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Routes.BASE_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {
  private final UserService userService;

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

  // @PatchMapping("/password")
  // public ResponseEntity<UserResponse> edit(@RequestBody PatchUser patch) {}
}
