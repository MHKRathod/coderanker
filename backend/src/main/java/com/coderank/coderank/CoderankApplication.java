package com.coderank.coderank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class CoderankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoderankApplication.class, args);
	}

}
