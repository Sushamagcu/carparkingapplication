package com.sample.applicaton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sample.applicaton.service.CarParkingService;


@SpringBootApplication
public class CarParkingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CarParkingApplication.class, args);
		
		context.getBean(CarParkingService.class).init(); 
	}


}
