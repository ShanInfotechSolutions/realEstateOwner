package com.shanInfotech.springBootMicroservicesOwnerClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.shanInfotech.springBootMicroservicesOwnerClient.repository.OwnerRepository;

@SpringBootTest
@TestPropertySource(properties = {
  // JWT needed by JwtService
  "jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
  "jwt.expiration-minutes=60",

  // keep service discovery off
  "eureka.client.enabled=false",
  "spring.cloud.discovery.enabled=false",

  // Keep DB/JPA OFF for this smoke test
  "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class SpringBootMicroservicesOwnerClientApplicationTests {

  // Provide a mock OwnerRepository so OwnerService can be created
  @TestConfiguration
  static class Mocks {
    @Bean
    OwnerRepository ownerRepository() {
      return Mockito.mock(OwnerRepository.class);
    }
  }

  @Test
  void contextLoads() {}
}
