package com.edf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EdfApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EdfApplication.class);

		Map<String, Object> defaultProperties = getDefaultProperties();
		app.setDefaultProperties(defaultProperties);
		app.run(args);
	}

	private static Map<String, Object> getDefaultProperties() {
		Map<String, Object> defaultProperties = new HashMap<>();
		defaultProperties.put("server.port", "8083");
		defaultProperties.put("server.address", "localhost");
		return defaultProperties;
	}

}
