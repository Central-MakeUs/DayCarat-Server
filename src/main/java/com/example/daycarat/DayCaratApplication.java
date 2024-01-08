package com.example.daycarat;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
		servers = {
				@Server(url = "https://www.daycarat.store/api", description = "Production Server URL"),
				@Server(url = "http://localhost:8080/api", description = "Dev Server URL")
		}
)
@SpringBootApplication
@EnableJpaAuditing
public class DayCaratApplication {

	public static void main(String[] args) {
		SpringApplication.run(DayCaratApplication.class, args);
	}

}
