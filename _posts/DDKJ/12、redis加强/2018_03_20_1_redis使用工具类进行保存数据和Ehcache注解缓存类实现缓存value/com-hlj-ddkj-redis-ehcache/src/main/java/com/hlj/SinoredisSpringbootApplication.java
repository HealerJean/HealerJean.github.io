package com.hlj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(value = "classpath:applicationContext.xml")
@SpringBootApplication
public class SinoredisSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinoredisSpringbootApplication.class, args);
	}
}
