package com.shorter;

import java.util.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shorter.security.JwtUtilities;

@SpringBootApplication
@EnableScheduling
public class ShorterApplication {

	private static JwtUtilities jwtUtilities;

	public ShorterApplication(JwtUtilities jwtUtilities) {
		ShorterApplication.jwtUtilities = jwtUtilities;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShorterApplication.class, args);

		Date date = new Date();
		System.out.println("Current Date: " + date);

		// String token = jwtUtilities.generateToken("admin@gmail.com", List.of("ADMIN"));
		// System.out.println("Token: " + token);
	}
}
