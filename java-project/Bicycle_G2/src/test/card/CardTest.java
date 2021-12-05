package test.card;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bike.ElectricBike;
import bike.MechanicBike;
import card.InsufficientCreditException;
import card.VlibreCard;
import card.VmaxCard;
import user.User;

class CardTest {
	
	private static User u1;
	
	@BeforeAll
	static void setup() {
		u1 = new User("PaulLegrand");
	}

	@Test
	void testComputeRidePriceElectricBikeInt() {
		VlibreCard VLibre = new VlibreCard(u1);
		VmaxCard VMax = new VmaxCard(u1);
		ElectricBike bike = new ElectricBike();
		assertAll("Ride prices for electric bikes",
			() -> {
				assertAll("With no time credit",
					() -> assertEquals(0, VMax.computeRidePrice(bike, 15), "15 minutes"),
					() -> assertEquals(3, VLibre.computeRidePrice(bike, 120), "120 minutes"),
					() -> assertEquals(1, VMax.computeRidePrice(bike, 120), "120 minutes"),
					() -> assertEquals(0.25, VLibre.computeRidePrice(bike, 15), "15 minutes")
				);
			},
			() -> {
				VLibre.setTimeCredit(20);
				VMax.setTimeCredit(20);
				assertAll("With time credit",
					() -> assertEquals(1.5, VLibre.computeRidePrice(bike, 95), "95 minutes"),
					() -> assertEquals(0, VMax.computeRidePrice(bike, 75), "75 minutes"),
					() -> assertEquals(0, VLibre.getTimeCredit(), "Vlibre time credit"),
					() -> assertEquals(5, VMax.getTimeCredit(), "Vmax time credit")
				);
			}
		);
	}

	@Test
	void testComputeRidePriceMechanicBikeInt() {
		VlibreCard VLibre = new VlibreCard(u1);
		VmaxCard VMax = new VmaxCard(u1);
		MechanicBike bike = new MechanicBike();
		assertAll("Ride prices for mechanic bikes",
			() -> {
				assertAll("With no time credit",
					() -> assertEquals(0, VMax.computeRidePrice(bike, 15), "15 minutes"),
					() -> assertEquals(1, VLibre.computeRidePrice(bike, 120), "120 minutes"),
					() -> assertEquals(1, VMax.computeRidePrice(bike, 120), "120 minutes"),
					() -> assertEquals(0, VLibre.computeRidePrice(bike, 15), "15 minutes")
				);
			},
			() -> {
				VLibre.setTimeCredit(20);
				VMax.setTimeCredit(20);
				assertAll("With 20 minutes time credit",
					() -> assertEquals(0.25, VLibre.computeRidePrice(bike, 95), "95 minutes"),
					() -> assertEquals(0, VMax.computeRidePrice(bike, 75), "75 minutes"),
					() -> assertEquals(0, VLibre.getTimeCredit(), "Vlibre time credit"),
					() -> assertEquals(5, VMax.getTimeCredit(), "Vmax time credit")
				);
			}
		);
	}

	@Test
	void testSetTimeCredit() {
		VlibreCard Vlibre = new VlibreCard(u1);
		Vlibre.setTimeCredit(10);
		assertEquals(10, Vlibre.getTimeCredit());
	}

	@Test
	void testAddCredit() {
		VlibreCard vlibre = new VlibreCard(u1);
		VmaxCard vmax = new VmaxCard(u1);
		vlibre.addCredit(10);
		vmax.addCredit(10);
		assertAll("Adding Time Credit",
			() -> assertEquals(10, vlibre.getTimeCredit()),
			() -> assertEquals(10, vmax.getTimeCredit())
		);
	}

	@Test
	void testUseCredit() {
		VlibreCard vlibre = new VlibreCard(u1);
		VmaxCard vmax = new VmaxCard(u1);
		vlibre.addCredit(10);
		vmax.addCredit(10);
		assertAll("Using time credit",
			() -> {
				vlibre.useCredit(5);
				vmax.useCredit(5);
				assertAll("When there is enough credit",
					() -> assertEquals(5, vlibre.getTimeCredit()),
					() -> assertEquals(5, vmax.getTimeCredit())
				);
			},
			() -> {
				assertAll("When the credit is insufficient",
					() -> assertThrows(InsufficientCreditException.class, () -> { vlibre.useCredit(20); }),
					() -> assertThrows(InsufficientCreditException.class, () -> { vmax.useCredit(20); })
				);
			}
		);
	}

}
