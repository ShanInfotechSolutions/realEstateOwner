package com.shanInfotech.springBootMicroservicesOwnerClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
   UserDetailsService userDetailsService(PasswordEncoder encoder) {
    return new InMemoryUserDetailsManager(
        User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build(),
        User.withUsername("user").password(encoder.encode("user123")).roles("USER").build()
    );
  }

  @Bean
   PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
 DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder encoder) {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
    p.setUserDetailsService(uds);
    p.setPasswordEncoder(encoder);
    return p;
  }

  @Bean
   AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

//SecurityConfig.java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 http
   .csrf(csrf -> csrf.disable())
   .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
   .authorizeHttpRequests(auth -> auth
     .requestMatchers("/api/auth/**", "/actuator/health", "/api/owners/health").permitAll()

     // READ: any authenticated user
     .requestMatchers(HttpMethod.GET, "/api/owners/**").hasAnyRole("USER","ADMIN")

     // WRITE: only ADMIN
     .requestMatchers(HttpMethod.POST, "/api/owners/**").hasRole("ADMIN")
     .requestMatchers(HttpMethod.PUT,  "/api/owners/**").hasRole("ADMIN")
     .requestMatchers(HttpMethod.DELETE,"/api/owners/**").hasRole("ADMIN")

     .anyRequest().authenticated()
   )
   .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
 return http.build();
}

}

