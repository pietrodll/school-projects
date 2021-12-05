package test.ui.clui;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bike.ElectricBike;
import bike.MechanicBike;
import card.Card;
import card.VmaxCard;
import ride.Network;
import station.StandardStation;
import station.Station;
import tools.NegativeTimeException;
import tools.Point;
import ui.clui.CommandLineDisplay;
import user.User;
import user.UserStat;

class CommandLineDisplayTest {
	
	static CommandLineDisplay cld;
	static Network net;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		cld = new CommandLineDisplay();
	}

	@Test
	void testDisplayStation() throws NegativeTimeException {
		Station s = new StandardStation(new Point(1.055645,10.54896), net);
		assertAll(
			() -> assertEquals("Station: id:0\n" + "\tPosition: x=1.056 y=10.549\n" + "Slots: 0\n", cld.display(s)),
			() -> {
				s.addSlot(3);
				LocalDateTime time = LocalDateTime.of(2019, 01, 01, 00, 00);
				s.getParkingSlots().get(1).setBike(new ElectricBike(), time);
				s.getParkingSlots().get(2).setBike(new MechanicBike(), time);
				String disp = cld.display(s);
				String expected = "Station: id:0\n"
					+ "\tPosition: x=1.056 y=10.549\n"
					+ "Slots: 3\n"
					+ "\tid:0\tOnline\tFree\n"
					+ "\tid:1\tOnline\tOccupied\tBike id:0\tType: Electric\n"
					+ "\tid:2\tOnline\tOccupied\tBike id:1\tType: Mechanic\n";
				assertEquals(expected, disp);
			}
		);
	}

	@Test
	void testDisplayCard() {
		String expected = "Card: id:0\n"
			+ "\tOwner: Pietro\n"
			+ "\tType: Vmax\n"
			+ "\tCredit: 10 minutes\n";
		Card card = new VmaxCard(new User("Pietro"));
		card.addCredit(10);
		String disp = cld.display(card);
		assertEquals(expected, disp);
	}

	@Test
	void testDisplayUser() {
		String expected = "User: id:1\n"
				+ "\tName: Chloe\n"
				+ "\tTotal amount paid: 3.54 euros\n"
				+ "\tTotal number of rides: 2\n"
				+ "\tTotal ride time: 50 minutes\n"
				+ "\tTotal credit earned: 10 minutes\n";
		User user = new User("Chloe");
		UserStat us = user.getUserStat();
		us.addAmount(3.5368);
		us.addCreditEarned(10);
		us.addRide(); us.addRide();
		us.addTime(50);
		String disp = cld.display(user);
		assertEquals(expected, disp);
	}

}
