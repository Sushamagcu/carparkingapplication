# carparkingapplication

This application primarly deals with parking and exiting vehicle from a parking slot.

This application has 4 API's

1) /allotParkingSlot/{vehicleId}   : This API call allocates available parking slot if the vehicle number is valid. If the parking slots are full it returns "Parking is FULL"
2) /getParkingSlotsStatus          : This API call gets the total available and occupied slots at current time.
3) /exitParking/{vehicleId}        : This API call disallocates the parking slot for vehicle is parked and results in payment for parking at the rate of £2 per hour
   NOTE: Parked for more than 45 minutes is considered as an hour
5) /getParkedVehicalDetails        : This API call fetches parked vehicle list as JSON
