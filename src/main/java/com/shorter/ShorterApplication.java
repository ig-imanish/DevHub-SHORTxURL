package com.shorter;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class ShorterApplication {


	public static void main(String[] args) {
		SpringApplication.run(ShorterApplication.class, args);
		
		 // Load environment variables from .env file BEFORE Spring starts
		 try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(e -> {
                System.setProperty(e.getKey(), e.getValue());
                System.out.println("Loaded env var: " + e.getKey());
            });
        } catch (Exception e) {
            System.err.println("Warning: Failed to load .env file. " + e.getMessage());
        }
        
		Date date = new Date();
		System.out.println("Current Date: " + date);
	}
}
