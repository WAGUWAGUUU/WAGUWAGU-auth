package com.example.wgwg_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@SpringBootApplication
public class WgwgAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WgwgAuthApplication.class, args);
	}
	@Bean
	public RecordMessageConverter converter(){
		return new JsonMessageConverter();
	}
}
