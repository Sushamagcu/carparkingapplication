package com.sample.applicaton.model;

import java.time.LocalDateTime;

/**
 * Pojo class for storing parking details
 * @author sushama gaddam
 *
 */
public class ParkingSlot {

	private int parkingId;
	private boolean isAvailable = true;
	private String vehicleId;
	private LocalDateTime entryDateTime;
	private LocalDateTime exitDateTime;
	private double charge;

	public ParkingSlot(int parkingId) {
		super();
		this.parkingId = parkingId;
	}

	public int getParkingId() {
		return parkingId;
	}

	public void setParkingId(int parkingId) {
		this.parkingId = parkingId;
	}


	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public LocalDateTime getEntryDateTime() {
		return entryDateTime;
	}

	public void setEntryDateTime(LocalDateTime entryDateTime) {
		this.entryDateTime = entryDateTime;
	}

	public LocalDateTime getExitDateTime() {
		return exitDateTime;
	}

	public void setExitDateTime(LocalDateTime exitDateTime) {
		this.exitDateTime = exitDateTime;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	@Override
	public String toString() {
		return "ParkingSlot [parkingId=" + parkingId + ", isavailable=" + isAvailable + ", vehicle=" + vehicleId
				+ ", entryDateTime=" + entryDateTime + ", exitDateTime=" + exitDateTime + ", charge=" + charge+ "]";
	}

	/**
	 * Method to reset parking slot values after vehicke exit
	 * @param slot
	 */
	public static void resetParkingSlot(ParkingSlot slot) {
		slot.setAvailable(true);
		slot.setEntryDateTime(null);
		slot.setExitDateTime(null);
		slot.setVehicleId(null);
		slot.setCharge(0);
	}
}
