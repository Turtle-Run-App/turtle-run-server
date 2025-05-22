package com.turtleRun.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TurtleRunBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurtleRunBeApplication.class, args);
	}

}
