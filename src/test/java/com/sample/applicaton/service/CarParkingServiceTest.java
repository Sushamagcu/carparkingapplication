package com.sample.applicaton.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sample.applicaton.model.ParkingSlot;

class CarParkingServiceTest {

	static CarParkingService service = new CarParkingService();

	@BeforeAll
	public static void init() {
		System.out.println("BeforeAll init() method called");
		service.init();
	}

	// validateVehicleIdTest()- validating valid vehicle number
	@Test
	public void Test1() {
		boolean valid = service.validateVehicleId("LM2323");
		assertTrue(valid);
	}

	// validateVehicleIdTest()-Validating invalid vehicle number
	@Test
	public void Test2() {
		boolean valid = service.validateVehicleId("LM%2323");
		assertFalse(valid);
	}

	// getParkingSlotsStatusTest()- parking slots status initially
	@Test
	public void Test3() {
		String message = service.getParkingSlotsStatus();
		System.out.println(message);
		assertEquals("Total Available Slots :: 100" + "\nTotal Occupied Slots  :: 0", message);
	}

	// checkForSlotAvailibilityTest()- check for slot availability
	@Test
	public void Test4() {
		boolean available = service.checkForSlotAvailibility();
		assertTrue(available);
	}

	// getParkedVehicalDetailsTest() -- parked vehicle details when no vehicle
	// parked
	@Test
	public void Test5() {
		List<ParkingSlot> list = service.getParkedVehicalDetails();

		assertEquals(list.size(), 0);
	}

	// fetchParkingSlotOfVehicle -- parking slot when vehicle not parked
	@Test
	public void Test6() {
		Optional<ParkingSlot> list = service.fetchParkingSlotOfVehicle("LM2323");

		assertEquals(list.isPresent(), false);
	}

	// allocateParkingSlot -- Assign parking slot
	@Test
	public void Test7() {
		String message = service.allocateParkingSlot("LM2323");
		System.out.println(message);
		assertEquals("Parking slot allocated  for Vehicle LM2323 is :1", message);
	}

	// allocateParkingSlot - for already parked vehicle
	@Test
	public void Test8() {
		String message = service.allocateParkingSlot("LM2323");
		assertEquals("Parking slot for Vehicle LM2323 is already allocated at :1", message);
	}

	// disallocateParkingSlot-- exit parking for parked vehicle
	@Test
	public void Test9() {
		String message = service.disallocateParkingSlot("LM2323");
		System.out.println("message");
		assertEquals("Payment for vehicle LM2323 parked for 0 hours is £0", message);
	}

	// disallocateParkingSlot--Exit parking for not parked vehicle
	@Test
	public void Test_10() {
		String message = service.disallocateParkingSlot("LM2323");
		System.out.println("message");
		assertEquals("Vehicle LM2323 is not allocated with any parking slot.", message);
	}

	// allocateParkingSlot-- When parking slot is full
	@Test
	public void Test_11() {
		String message;
		for (int i = 1; i <= 100; i++) {
			message = service.allocateParkingSlot("LM" + i);
		}
		message = service.allocateParkingSlot("LM 101");
		;
		System.out.println("message");
		assertEquals("Parking is FULL", message);
	}

	// getParkingSlotsStatusTest()- parking slots status when parking is full
	@Test
	public void Test_12() {
		String message = service.getParkingSlotsStatus();
		System.out.println(message);
		assertEquals("Total Available Slots :: 0" + "\nTotal Occupied Slots  :: 100", message);
	}
	
	// getParkedVehicalDetailsTest() -- parked vehicle details when vehicle is available
		@Test
		public void Test_13() {
			List<ParkingSlot> list = service.getParkedVehicalDetails();

			assertEquals(list.size(), 100);
		}

}
