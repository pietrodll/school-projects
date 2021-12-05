package test.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import bike.Bike;
import bike.ElectricBike;
import card.Card;
import card.VlibreCard;
import ride.Itinerary;
import ride.Network;
import ride.path.FastestPathStrategy;
import ride.path.PathStrategy;
import station.NoOngoingRideException;
import station.OngoingRideException;
import station.Station;
import station.StationFactory;
import station.StationSamePositionException;
import station.TypeStationException;
import tools.NegativeTimeException;
import tools.NullDateException;
import tools.Point;
import user.User;

public class UserTest {
	
	Network net = new Network();
	Point p1 = new Point (3, 9);
	Point p2 = new Point (0, 6);
	Point p3 = new Point (1, 0);
	Point p4 = new Point (5, 2);
	User u1 = new User("PietroDellino");
	User u2 = new User("ChloéGentgen", p1);
	User u3 = new User("PietroDellino, p2");
	Bike b1 = new ElectricBike();
	Bike b2 = new ElectricBike();
	Card c1 = new VlibreCard(u1);
	LocalDateTime t1 = LocalDateTime.of(2019,  03, 10, 16, 00);
	LocalDateTime t2 = LocalDateTime.of(2019,  03, 10, 16, 30);
	LocalDateTime t3 = LocalDateTime.of(2019,  03, 10, 17, 00);
	LocalDateTime t4 = LocalDateTime.of(2019,  03, 10, 17, 30);
	StationFactory sf = new StationFactory(net);
	
	@Test
	void testDifferentIDUsers() {
		assertNotEquals(u1.getId(), u2.getId());
		assertFalse(u1.equals(u2));
	}

	@Test
	void testDifferentUsers() {
		assertNotEquals(u1, u3);
	}
	

	@Test
	void testStartOngoingRide () throws OngoingRideException {
		u1.startOngoingRide(net, b1, t1, c1);
		assertAll("Checks if the created ride is correct",
				() -> assertNotNull(u1.getOngoingRide()),
				() -> assertEquals(net, u1.getOngoingRide().getNet()),
				() -> assertEquals(b1, u1.getOngoingRide().getBike()),
				() -> assertEquals(t1, u1.getOngoingRide().getStartRide()),
				() -> assertEquals(null, u1.getOngoingRide().getEndRide()),
				() -> assertEquals(c1, u1.getOngoingRide().getCard()),
				() -> assertEquals(0, u1.getUserStat().getNumberRides())
		);
	}
	
	@Test
	void testCannotStartSecondOngoingRide () throws OngoingRideException {
		u1.startOngoingRide(net, b1, t1, c1);
		assertThrows(OngoingRideException.class,
			() -> u1.startOngoingRide(net, b2, t2, c1)
		);		
	}
	
	
	@Test
	void testEndOngoingRide () throws Exception {
		u1.startOngoingRide(net, b1, t1, c1);
		u1.endOngoingRide(t2);
		assertAll("Checks if the ended ride is correct",
				() -> assertNull(u1.getOngoingRide()),
				() -> assertEquals(1, net.getRideHistory().size()),
				() -> assertEquals(t2, net.getRideHistory().get(0).getEndRide()),
				() -> assertEquals(30, net.getRideHistory().get(0).getRideTime()),
				() -> assertEquals (1, u1.getUserStat().getNumberRides()),
				() -> assertEquals(30, u1.getUserStat().getTotalTime()),
				() -> assertEquals(0.5, u1.getUserStat().getTotalAmount())	
		);
	}
	
	@Test
	void testCannotEndOngoingRideIfNoRide () throws NoOngoingRideException {
		assertThrows(NoOngoingRideException.class,
			() -> u1.endOngoingRide(t2)
		);		
	}
	
	@Test
	void testSetItinerary () throws OngoingRideException, NegativeTimeException, NullDateException, NoOngoingRideException, TypeStationException, StationSamePositionException {
		Station s1 = sf.createStation("Standard", p3);
		Station s2 = sf.createStation("Plus", p4);
		s1.addSlot();
		s2.addSlot();
		s1.getParkingSlots().get(0).setBike(b1, t1);
		PathStrategy ps = new FastestPathStrategy(net);
		Itinerary i1 = u1.calculateItinerary(p1, p2, ps);
		u1.setItinerary(i1);
		u1.startOngoingRide(net, b1, t2, c1);
		
		assertAll("Has an itinerary",
				() -> assertNotNull(u1.getItinerary())
		);
		
		u1.endOngoingRide(t4);
		assertAll("Has no itinerary",
				() -> assertNull(u1.getItinerary())
		);
		
		
	}
	
}
