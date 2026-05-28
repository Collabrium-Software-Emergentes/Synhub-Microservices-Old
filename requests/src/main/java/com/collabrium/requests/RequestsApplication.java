package com.collabrium.requests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
		exclude = {
				UserDetailsServiceAutoConfiguration.class
		}
)
@EnableFeignClients
@EnableJpaAuditing
public class RequestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequestsApplication.class, args);
	}

}
