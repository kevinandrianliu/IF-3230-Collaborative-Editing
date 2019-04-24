package com.tugasbesar.classes;

public class VersionVector{
	private String computerId;
	private int counter;
	
	public VersionVector (String mComputerId, int mCounter) {
		computerId = mComputerId;
		counter = mCounter;
	}
	public String getId() {
		return computerId;
	}
	public int getCounter() {
		return counter;
	}
	public void setId(String mComputerId) {
		computerId = mComputerId;
	}
	public void setCounter(int mCounter) {
		counter = mCounter;
	}
	public void incrementCounter() {
		counter++;
	}
}