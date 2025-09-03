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

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder enc) {
    return new InMemoryUserDetailsManager(
      User.withUsername("admin").password(enc.encode("admin123")).roles("ADMIN").build(),
      User.withUsername("user").password(enc.encode("user123")).roles("USER").build()
    );
  }

  @Bean
  AuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder enc) {
    var p = new DaoAuthenticationProvider(uds); // new-style ctor
    p.setPasswordEncoder(enc);
    return p;
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationProvider provider,
      JwtAuthFilter jwtAuthFilter // ⬅️ inject here, not via constructor/field
  ) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
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
