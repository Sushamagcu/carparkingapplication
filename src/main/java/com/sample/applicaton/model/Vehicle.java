package com.sample.applicaton.model;

public class Vehicle {

	
	String vehicleNumber;
	String color;
	
	
	public Vehicle(String vehicleNumber, String color) {
		super();
		this.vehicleNumber = vehicleNumber;
		this.color = color;
	}
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}
