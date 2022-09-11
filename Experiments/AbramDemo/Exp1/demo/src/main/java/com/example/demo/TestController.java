package com.example.demo;

import java.util.List;

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
	
	
	@GetMapping()
	public List<TestData> getTest() {
		return access.allNames();
	}
	
	@GetMapping(path = "/job/{job}")
	public List<TestData> getjobs(@PathVariable("job") String job) {
		return access.getJobs(job);
	}
	
	@GetMapping(path = "/age/{age}")
	public List<TestData> getages(@PathVariable("age") String age) {
		return access.getAges(age);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		access.addMessage(message);
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}

	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody String username) {
		access.addMessage(username);
		return String.format("Hello, %s! You sent a post request with a requestbody!", username);
	}	
	
	@PostMapping("/postTest3")
	public TestData postTest3(@RequestBody TestData td) {
		TestData temp = new TestData(td.getName(), td.getJob(), td.getAge());
		access.addMessage(temp);
		return temp;
	}	
	
	@GetMapping(path = "{index}")
	public TestData get(@PathVariable("index") int index) { //@RequestParam(value = "index", defaultValue = "0") int index
		return access.getMessage(index);
	}
	
	@DeleteMapping(path = "{index}")
	public List<TestData> deleteTest(@PathVariable("index") int id) {
		access.deleteMessage(id);
		return access.allNames();
	}
	

	@PutMapping(path = "{id}/name")
	public List<TestData> putName(@PathVariable("id") int id, @RequestBody String username) {
		access.updateName(id, username);
		return access.allNames();
	}
	
	@PutMapping(path = "{id}/job")
	public List<TestData> putJob(@PathVariable("id") int id, @RequestBody String job) {
		access.updateJob(id, job);
		return access.allNames();
	}

	@PutMapping(path = "{id}/age")
	public List<TestData> putAge(@PathVariable("id") int id, @RequestBody String age) {
		access.updateAge(id, age);
		return access.allNames();
	}
}
