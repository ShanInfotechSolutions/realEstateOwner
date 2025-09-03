package com.shanInfotech.springBootMicroservicesOwnerClient.auth;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.shanInfotech.springBootMicroservicesOwnerClient.config.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService uds) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userDetailsService = uds;
  }

  public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
  public record LoginResponse(String token, String tokenType) {}

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
      var user = userDetailsService.loadUserByUsername(req.username());
      String token = jwtService.generateToken(user);
      return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
    } catch (AuthenticationException ex) {
      return ResponseEntity.status(401).build();
    }
  }
}
