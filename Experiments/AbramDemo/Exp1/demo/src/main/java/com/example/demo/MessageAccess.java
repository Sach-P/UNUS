package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class MessageAccess {
	
	private ArrayList<TestData> names = new ArrayList<TestData>();
	
	public MessageAccess() {
		
	}
	
	public void addMessage(String name) {
		names.add(new TestData(name));
	}
	
	public void addMessage(TestData td) {
		names.add(td);
	}
	
	public TestData getMessage(int id) {
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getID() == id) {
				return names.get(i);
			}
		}
		return new TestData("bad ID");
	}
	
	public List<TestData> allNames() {
		return names;
	}
	
	public void deleteMessage(int id) {
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getID() == id) {
				names.remove(i);
				return;
			}
		}
	}
	
	public void updateName(int id, String newName) {
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getID() == id) {
				names.get(i).updateName(newName);
				return;
			}
		}
	}
	public void updateJob(int id, String newJob) {
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getID() == id) {
				names.get(i).updateJob(newJob);
				return;
			}
		}
	}
	public void updateAge(int id, String newAge) {
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getID() == id) {
				names.get(i).updateAge(newAge);
				return;
			}
		}
	}
	
	public List<TestData> getJobs(String job) {
		List<TestData> temp = new ArrayList<TestData>();
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getJob().equals(job)) {temp.add(names.get(i));};
		}
		return temp;
	}
	
	public List<TestData> getAges(String age) {
		List<TestData> temp = new ArrayList<TestData>();
		int n = names.size();
		for(int i = 0; i < n; i++) {
			if(names.get(i).getAge().equals(age)) {temp.add(names.get(i));};
		}
		return temp;
	}

}
