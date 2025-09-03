package com.shanInfotech.springBootMicroservicesOwnerClient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
  // minimal config for JwtService
  "jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
  "jwt.expiration-minutes=60",
  // keep cloud bits off during tests
  "eureka.client.enabled=false",
  "spring.cloud.discovery.enabled=false"
})
class SpringBootMicroservicesOwnerClientApplicationTests {

  @Test
  void contextLoads() {}
}

