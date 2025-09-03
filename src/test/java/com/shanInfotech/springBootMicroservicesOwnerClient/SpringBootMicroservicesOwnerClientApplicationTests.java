package com.shanInfotech.springBootMicroservicesOwnerClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
  // JWT for JwtService
  "jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
  "jwt.expiration-minutes=60",
  // turn off discovery/cloud
  "eureka.client.enabled=false",
  "spring.cloud.discovery.enabled=false",
  "spring.autoconfigure.exclude=" +
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
    "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration," +
    "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class SpringBootMicroservicesOwnerClientApplicationTests {

  @Test
  void contextLoads() {}
}
