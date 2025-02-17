package com.shorter;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShorterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShorterApplication.class, args);

		Date date = new Date();
		System.out.println(date);
		 
	}

}
