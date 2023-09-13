package com.sample.applicaton.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sample.applicaton.model.ParkingSlot;
import com.sample.applicaton.service.CarParkingService;


/**
 * @author Sushama Gaddam
 * Rest controller class to handle requests for Car Packing App
 */
@RestController
public class CarParkingController {

	@Autowired
	CarParkingService parkingService;

	
	/**
	 * This is entry point for API call for allocating parking slot for a vehicle
	 * @param vehicleId
	 * @return string denoting the parking slot allotment
	 */
	@GetMapping(value = "/allotParkingSlot/{vehicleId}")
	public String allotCarParkingSlot(@PathVariable("vehicleId") String vehicleId) {
		String result = parkingService.allocateParkingSlot(vehicleId);
		return result;
	}

	/**
	 * This is the entry point for API call for fetching the current available and occupied slots
	 * @return string denoting total available and occupied slots
	 */
	@GetMapping(value = "/getParkingSlotsStatus")
	public String getParkingSlotsStatus() {
		String status = parkingService.getParkingSlotsStatus();
		return status;

	}

	/**
	 * This is the entry point for API call for exiting the parking
	 * @param vehicleId
	 * @return string denoting the payment to be made for parking slot
	 */
	@GetMapping(value = "/exitParking/{vehicleId}")
	public String disallocateParkingSlot(@PathVariable("vehicleId") String vehicleId) {
		String paymentTicket = parkingService.disallocateParkingSlot(vehicleId);
		return paymentTicket;

	}
	
	/**
	 * This is an entry point for API to fetch the details of parked vehicles
	 * @return List<ParkingSlot> 
	 */
	@GetMapping(value ="/getParkedVehicalDetails")
	public List<ParkingSlot> getParkedVehicalDetails() {
		List<ParkingSlot> slotsList = parkingService.getParkedVehicalDetails();
		return slotsList;
		
	}

}
