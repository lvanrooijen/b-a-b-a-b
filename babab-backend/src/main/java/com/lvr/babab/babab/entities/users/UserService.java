package com.lvr.babab.babab.entities.users;

import com.lvr.babab.babab.configurations.JwtService;
import com.lvr.babab.babab.configurations.JwtToken;
import com.lvr.babab.babab.entities.authentication.dto.LoginRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterRequest;
import com.lvr.babab.babab.entities.authentication.dto.RegisterResponse;
import com.lvr.babab.babab.entities.users.dto.UserResponse;
import com.lvr.babab.babab.exceptions.FailedLoginException;
import com.lvr.babab.babab.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsManager {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public RegisterResponse register(@Valid RegisterRequest registerRequest) {
    User user =
        User.builder()
            .email(registerRequest.email())
            .password(passwordEncoder.encode(registerRequest.password()))
            .firstName(registerRequest.firstname())
            .lastName(registerRequest.lastname())
            .build();

    createUser(user);

    UserResponse userResponse = new UserResponse(user.id, user.username, user.email);
    String token = jwtService.generateTokenForUser(user);

    return new RegisterResponse(token, userResponse);
  }

  public JwtToken login(LoginRequest loginRequest) {
    User user =
        userRepository
            .findByEmailIgnoreCase(loginRequest.username())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
      return new JwtToken(jwtService.generateTokenForUser(user));
    } else {
      throw new FailedLoginException("Login Failed for user: " + loginRequest.username());
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmailIgnoreCase(username)
        .orElseThrow(() -> new UserNotFoundException("User by this email is not found"));
  }

  @Override
  public void createUser(UserDetails user) {
    userRepository.save((User) user);
  }

  @Override
  public void updateUser(UserDetails user) {
    userRepository.save((User) user);
  }

  @Override
  public void deleteUser(String username) {
    User user =
        userRepository
            .findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UserNotFoundException("User by this email is not found"));
    userRepository.delete(user);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    // TODO ME
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.findByEmailIgnoreCase(username).isPresent();
  }
}
