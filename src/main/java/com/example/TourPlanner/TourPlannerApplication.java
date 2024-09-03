package com.example.TourPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration(proxyBeanMethods = false)
public class TourPlannerApplication {

	public static void main(String[] args) {

		SpringApplication.run(TourPlannerApplication.class, args);
	}

}
