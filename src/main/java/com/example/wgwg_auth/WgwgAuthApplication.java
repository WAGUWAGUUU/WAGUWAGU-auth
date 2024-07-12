package com.example.wgwg_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class WgwgAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WgwgAuthApplication.class, args);
	}
}
