package com.shanInfotech.springBootMicroservicesOwnerClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootMicroservicesOwnerClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMicroservicesOwnerClientApplication.class, args);
	}

	@GetMapping("/getOwner")
	public String getOwner() {
		return "Hello Owner";
	}
}
