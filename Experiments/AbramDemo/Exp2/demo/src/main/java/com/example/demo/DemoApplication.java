package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication
public class DemoApplication {

	@RequestMapping("/")
	@ResponseBody
	String home()
	{
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(primarySource: DemoApplication.class, args);
		System.out.println("Hello World");
	}

}
