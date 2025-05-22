package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.entities.authentication.AuthenticationService;
import com.lvr.babab.babab.entities.users.dto.*;
import com.lvr.babab.babab.exceptions.authentication.AdminOnlyActionException;
import com.lvr.babab.babab.exceptions.users.DuplicateEmailException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final AuthenticationService authenticationService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponseMax> getAll() {
    return userRepository.findAll().stream().map(UserResponseMax::to).toList();
  }

  @PreAuthorize("hasRole('ROLE_ADMIN') or #id.equals(authentication.principal.id)")
  public UserResponseMax patchUser(Long id, PatchUser patch) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("[update user failed] reason=user id not found id=%s", id)));
    if (user.isAdmin()) {
      throw new AdminOnlyActionException(
          "[update user failed] reason=admin account may not be altered");
    }

    if (patch.firstname() != null) {
      user.setFirstName(patch.firstname());
    }

    if (patch.lastname() != null) {
      user.setLastName(patch.lastname());
    }

    if (patch.birthday() != null) {
      user.setBirthdate(patch.birthday());
    }

    if (patch.email() != null) {
      if (userRepository.findByEmailIgnoreCase(patch.email()).isPresent()) {
        throw new DuplicateEmailException(
            String.format(
                "[update user failed] reason=email already exists email=%s", patch.email()));
      }
    }
    authenticationService.updateUser(user);
    return UserResponseMax.to(user);
  }

  @PreAuthorize("hasRole('USER_ADMIN' or #id.equals(authentication.principal.id))")
  public UserResponseMax getById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format(
                            "[failed to get user by id] reason=user with this id not found id=%s",
                            id)));
    return UserResponseMax.to(user);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN' or #id.equals(authentication.principal.id))")
  public void delete(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format(
                            "[failed to delete user] reason=user with this id not found id=%s",
                            id)));
    userRepository.delete(user);
  }
}
