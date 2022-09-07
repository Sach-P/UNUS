package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
@RestController  
public class HelloWorldController   
{  
	@RequestMapping("/hello")  
	public String hello()   
	{  
		return "<html><h1 style=\"color: red;\">Hello <br> World</h1></html>";  
	}  
}  