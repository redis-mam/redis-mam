package com.dobe.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class RedisMamApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisMamApplication.class, args);
	}

}
