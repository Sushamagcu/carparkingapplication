package com.sample.applicaton.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sample.applicaton.model.ParkingSlot;

/**
 * This is a service class which handles the business logic for all the API calls
 * for Car Parking Application
 * @author sushama gaddam
 *
 */
@Service
public class CarParkingService {

	Logger logger = LoggerFactory.getLogger(CarParkingService.class);
	private static int TOTAL_PARKING_SLOTS = 100;
	private static int RATE_PER_HOUR = 2;
	private List<ParkingSlot> slotsList = new ArrayList<ParkingSlot>();

	/**
	 * This method intialized the prking slots with defaults values 
	 * on application startup
	 */
	public void init() {
		for (int i = 1; i <= TOTAL_PARKING_SLOTS; i++) {
			ParkingSlot slot = new ParkingSlot(i);
			slotsList.add(slot);
		}
		//Syncronize lists as we are details with multiple cars a same point
		slotsList = Collections.synchronizedList(slotsList);
		logger.info("CarParkingService::init- Total parking slots intialized >>{} ",slotsList.size());
	}


	/**
	 * This method deals with the logic for allocating parking slot for a valid vehicle entry
	 * @param vehicleId
	 * @return
	 */
	public String allocateParkingSlot(String vehicleId) {
		String allocatedMessage ="";
		//Validate vehicle number
		boolean validVehicleId = validateVehicleId(vehicleId);

		try {
			if (validVehicleId) {
				//Syncronized block to handle multiple parking slot requests at same time
				synchronized (slotsList) {
					Optional<ParkingSlot> slotAlreadyAllocated = fetchParkingSlotOfVehicle(vehicleId);
	
					if (!slotAlreadyAllocated.isPresent()) {
						logger.info("CarParkingService::allocateParkingSlot- slotAlreadyAllocated is FALSE");
						if (checkForSlotAvailibility()) {
							logger.info("CarParkingService::allocateParkingSlot- checkForSlotAvailibility is TRUE");
							Optional<ParkingSlot> slot = slotsList.stream().filter(s -> s.isAvailable()).findFirst();
							ParkingSlot parkingSlot = slot.get();
							parkingSlot.setVehicleId(vehicleId);
							parkingSlot.setAvailable(false);
							parkingSlot.setEntryDateTime(LocalDateTime.now());
							allocatedMessage = "Parking slot allocated  for Vehicle " + vehicleId + " is :"
									+ parkingSlot.getParkingId();
							logger.info("CarParkingService::allocateParkingSlot- Parking slot allocated for ",vehicleId," at -- ",parkingSlot.getParkingId());
						} else {
							allocatedMessage = "Parking is FULL";
						}
					} else {
						allocatedMessage = "Parking slot for Vehicle " + vehicleId + " is already allocated at :"
								+ slotAlreadyAllocated.get().getParkingId();
					}
				}
			} else {
				allocatedMessage = "Vehicle Number is not valid. Please enter valid vehicle number.";
			}
		}catch (Exception e) {
			logger.debug("CarParkingService::allocateParkingSlot-",e.getMessage());
		}
		return allocatedMessage;

	}

	
	/**
	 * This method deals with the logic for dis-allocating parking slot of parked vehicle 
	 * @param vehicleId
	 * @return
	 */
	public String disallocateParkingSlot(String vehicleId) {

		String disAllocatedMessage = "";
		long parkedHours;
		long parkingBill;
			try {
			//Syncronized block to handle multiple parking slot exit requests at same time
				synchronized (slotsList) {
					Optional<ParkingSlot> slotAlreadyAllocated = fetchParkingSlotOfVehicle(vehicleId);
					if (slotAlreadyAllocated.isPresent()) {
						logger.info("CarParkingService::disallocateParkingSlot- slotAlreadyAllocated is TRUE");
		
						ParkingSlot slot = slotAlreadyAllocated.get();
						slot.setExitDateTime(slot.getEntryDateTime());
						parkedHours = Duration.between(slot.getExitDateTime(), slot.getEntryDateTime()).toHours();
						parkedHours = parkedHours
								+ (slot.getExitDateTime().getMinute() - slot.getEntryDateTime().getMinute() > 45 ? 1 : 0);
						parkingBill = RATE_PER_HOUR * parkedHours;
		
						ParkingSlot.resetParkingSlot(slot);
		
						disAllocatedMessage = "Payment for vehicle " + vehicleId + " parked for " + parkedHours + " hours is £"
								+ parkingBill;
						logger.info("CarParkingService::disallocateParkingSlot- Slot sucesfully dis allocated for vehicle",vehicleId);
		
					} else {
						disAllocatedMessage = "Vehicle " + vehicleId + " is not allocated with any parking slot.";
					}
				}
			}catch (Exception e) {
				logger.debug("CarParkingService::disallocateParkingSlot-",e.getMessage());
			}
		return disAllocatedMessage;
	}
	
	

	/**
	 * This method checks if the vehicle has parked already and returns its details if its parked
	 * @param vehicleId
	 * @return
	 */
	public Optional<ParkingSlot> fetchParkingSlotOfVehicle(String vehicleId) {
		List<String> slot = slotsList.stream().map(s -> s.getVehicleId()).collect(Collectors.toList());
		
		if (slot.contains(vehicleId)) {
			logger.info("CarParkingService::fetchParkingSlotOfVehicle- Vehicle",vehicleId," is alredy parked");
			return slotsList.stream()
					.filter(s -> s.getVehicleId() != null && s.getVehicleId().equalsIgnoreCase(vehicleId)).findFirst();
		} else
			logger.info("CarParkingService::fetchParkingSlotOfVehicle- Vehicle",vehicleId," is NOT parked yet");
			return Optional.empty();
	}
	

	/**
	 * This method fetched all the parked vehicle details
	 * @return
	 */
	public List<ParkingSlot> getParkedVehicalDetails() {
		List<ParkingSlot> occupiedSlots = slotsList.stream().filter(s -> !s.isAvailable()).collect(Collectors.toList());
		logger.info("CarParkingService::getParkedVehicalDetails- Parked vehicles count >>  {}",occupiedSlots.size());
		return occupiedSlots;
	}
	
	/**
	 * This method deals with checking for current available parking slots
	 * @return
	 */
	public boolean checkForSlotAvailibility() {
		long slot = slotsList.stream().filter(s -> s.isAvailable()).count();
		logger.info("CarParkingService::checkForSlotAvailibility- slots available count >> {}",slot);
		return slot > 0 ? true : false;
	}
	

	/**
	 * This method deals with code for validating vehicle number
	 * @param vehicleId
	 * @return
	 */
	public boolean validateVehicleId(String vehicleId) {
		if (vehicleId != null && (vehicleId.length() > 0 && vehicleId.length() <= 7)) {
			for (int i = 0; i < vehicleId.length(); i++) {
				if (!Character.isLetter(vehicleId.charAt(i)) 
						&& !Character.isDigit(vehicleId.charAt(i)) 
						&& !Character.isWhitespace(vehicleId.charAt(i)))
				{
					logger.info("CarParkingService::validateVehicleId- Vehicle number is Not Valid");
					return false;
				}
			}
		}
		logger.info("CarParkingService::validateVehicleId- Vehicle number is Valid");
		return true;
	}
	

	/**
	 * This method deals with finding the current available and occupied parking slots
	 * @return
	 */
	public String getParkingSlotsStatus() {
		StringBuilder message = new StringBuilder();
		try {
			long totalSlotsOccupied = slotsList.stream().filter(s->!s.isAvailable()).count();
			long totalSlotsAvailable = slotsList.stream().filter(s->s.isAvailable()).count();
			
			message.append("Total Available Slots :: "+totalSlotsAvailable);
			message.append("\nTotal Occupied Slots  :: "+totalSlotsOccupied);
			logger.info("CarParkingService::getParkingSlotsStatus- Status >> {}",message.toString());
			return message.toString();
		}catch (Exception e) {
			logger.debug("CarParkingService::getParkingSlotsStatus-",e.getMessage());

		}
		return message.toString();
	}

}
