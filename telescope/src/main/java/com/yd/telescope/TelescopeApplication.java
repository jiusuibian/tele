package com.yd.telescope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TelescopeApplication {
	public static void main(String[] args) {
		SpringApplication.run(TelescopeApplication.class, args);
	}
}
