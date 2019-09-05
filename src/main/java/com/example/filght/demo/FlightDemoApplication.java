package com.example.filght.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FlightDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightDemoApplication.class, args);
	}

	/**
	 * RestTemplate initialization for rest call
	 * 
	 * @return
	 */
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
