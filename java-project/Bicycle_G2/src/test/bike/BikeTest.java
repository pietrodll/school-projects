package test.bike;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bike.*;

class BikeTest {

	/**
	 * Tests if the IDs of two bikes are different
	 */
	@Test
	void testGetId() {
		Bike electricBike = new ElectricBike();
		Bike mechanicBike = new MechanicBike();
		assertNotEquals(electricBike.getId(),mechanicBike.getId());
	}
	
	@Test
	void instanceTest() {
		Bike electricBike = new ElectricBike();
		Bike mechanicBike = new MechanicBike();
		assertAll(
			() -> assertTrue(mechanicBike instanceof MechanicBike),
			() -> assertTrue(electricBike instanceof ElectricBike)
		);
	}

}
