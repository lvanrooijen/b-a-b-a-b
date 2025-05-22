package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.configurations.Routes;
import com.lvr.babab.babab.entities.users.dto.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.USERS)
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponseMax>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseMax> updateUser(
      @PathVariable Long id, @RequestBody PatchUser patch) {
    return ResponseEntity.ok(userService.patchUser(id, patch));
  }

  // user inloggen

  // user ww veranderen

  // user eigen account verwijderen
}
