package com.shanInfotech.springBootMicroservicesOwnerClient.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize on controllers if you use it
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder enc) {
    return new InMemoryUserDetailsManager(
        User.withUsername("admin").password(enc.encode("admin123")).roles("ADMIN").build(),
        User.withUsername("user").password(enc.encode("user123")).roles("USER").build()
    );
  }

  // New-style: pass UDS in constructor (no-arg ctor/setter are deprecated)
  @Bean
  AuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder enc) {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider(uds);
    p.setPasswordEncoder(enc);
    return p;
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider provider) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      // .cors(Customizer.withDefaults()) // enable if calling service directly from React (no gateway/proxy)
      .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**", "/api/owners/health", "/actuator/health").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/owners/**").hasAnyRole("USER","ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/owners/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/owners/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/owners/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .authenticationProvider(provider)
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
