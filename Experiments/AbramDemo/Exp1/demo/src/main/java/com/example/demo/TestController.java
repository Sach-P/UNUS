package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/messages")
@RestController
public class TestController {
	
	public MessageAccess access = new MessageAccess();
	
	
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		access.addMessage(message);
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		//
		access.addMessage(message);
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}

	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {

		access.addMessage(testData.getMessage());
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}	
	
	@GetMapping(path = "{index}")
	public String get(@PathVariable("index") int index) { //@RequestParam(value = "index", defaultValue = "0") int index
		return String.format("%s", access.getMessage(index));
	}
	
	@GetMapping("/all")
	public String[] getAll() {
		return access.allMessages();
	}
	
	@DeleteMapping(path = "{index}")
	public String[] deleteTest(@PathVariable("index") int index) {
		access.deleteMessage(index);
		return access.allMessages();
	}
	
	@PutMapping(path = "{name}")
	public String[] putTest(@PathVariable("name") String currname, @RequestBody String username) {
		access.updateName(currname, username);
		return access.allMessages();
	}
}
