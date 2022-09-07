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
	
	@RequestMapping("/bye")
	public String bye() 
	{
		return "<html><p style=\"font-family: Cursive;\">Goodbye for now :(<br></p> <img src=\"https://image.petmd.com/files/2021-11/sad-dog.jpg\" "
				+ "alt=\"sad dog\" height=\"500\" width=\"700\"/> </html>";
	}
}  