package com.example.cricketApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableCaching
@SpringBootApplication
public class CricketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketAppApplication.class, args);
		System.out.println("Welcome to cricket game");
	}
}
