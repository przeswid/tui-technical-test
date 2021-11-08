package com.tui.proof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PilotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PilotesApplication.class, args);
	}

}
