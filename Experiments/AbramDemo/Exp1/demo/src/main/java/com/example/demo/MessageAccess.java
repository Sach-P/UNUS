package com.example.demo;

public class MessageAccess {
	
	private String[] messages = new String[10];
	private int index;
	
	public MessageAccess() {
		for(int i = 0; i < 10; i++) {
			messages[i] = " ";
		}
	}
	
	public void addMessage(String s) {
		messages[index] = s;
		if(index<9) { index++;}
	}
	
	public String getMessage(int i) {
		if(i < 0) {return messages[0]; }
		if(i > 9) {return messages[9]; }
		return messages[i];
	}
	
	public String[] allMessages() {
		return messages;
	}
	
	public void deleteMessage(int n) {
		index--;
		if(n < 0) { n = 0; }
		if(n > 9) { n = 9; }
		for(int i = n; i < 9; i++) {
			messages[i] = messages[i+1];
		}
		messages[9] = " ";
	}
	
	public void updateName(String name, String newName) {
		for(int i = 0; i < 9 ; i++) {
			if(messages[i].equals(name))
				messages[i] = newName;
		}
	}

}
