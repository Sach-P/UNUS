package com.example.demo;

import java.util.Random;

public class TestData {
	
	private String name;
	private int id;
	private String job;
	private String age;
	
	
	Random rand = new Random();
	
	public TestData() {}
	
	public TestData(String name, String job, String age) {
		this.name = name;
		this.id = rand.nextInt(1000);
		this.job = job;
		this.age = age;	
	}
	
	public TestData(String name) {
		this.name = name;
		this.id = rand.nextInt(1000);
		this.job = " ";
		this.age = "1";	
	}

	public String getName() {
		return name;
	}
	
	public void updateName(String nName) {
		this.name = nName;
	}
	
	public int getID() {
		return id;
	}
	
	public String getJob() {
		return job;
	}
	
	public void updateJob(String job) {
		this.job = job;
	}
	
	public String getAge() {
		return age;
	}
	
	public void updateAge(String age) {
		this.age = age;
	}
}
