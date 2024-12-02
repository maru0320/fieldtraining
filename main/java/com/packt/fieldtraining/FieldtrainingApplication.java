package com.packt.fieldtraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.packt.fieldtraining")
@EntityScan(basePackages = "com.packt.fieldtraining.data.entity")
public class FieldtrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FieldtrainingApplication.class, args);
	}

}
