package com.example.demo;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootProject3Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootProject3Application.class, args);
	}

	@Test
    void contextLoads() {
        System.out.println("✅ Spring Boot 測試環境載入成功！");
    }

}
