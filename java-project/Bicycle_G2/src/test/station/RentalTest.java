package test.station;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import bike.Bike;
import bike.ElectricBike;
import bike.MechanicBike;
import card.*;
import ride.Itinerary;
import ride.Network;
import ride.path.FastestPathStrategy;
import ride.path.PathStrategy;
import station.*;
import tools.NegativeTimeException;
import tools.Point;
import user.User;

public class RentalTest {
	
	private Network net;
	private StationFactory sf;
	private CardFactory cf;
	private User u1;
	private User u2;
	
	@Test
	void setUp() throws TypeStationException, StationSamePositionException {
		net = new Network();
		sf = new StationFactory(net);
		cf = new CardFactory(net);
		
		u1 = new User ("DellinoPietro");
		u2 = new User ("GentgenChloé");
		
		Point p1 = new Point (1,3);
		Point p2 = new Point (2,3);
		Point p3 = new Point (4,9);
		Point p4 = new Point (5,2);
		Point p5 = new Point (5,9);
		
		Station s1 = sf.createStation("Standard", p1);
		Station s2 = sf.createStation("Standard", p2);
		Station s3 = sf.createStation("Plus", p3);
		
		Card c1 = cf.createCard(1,u1);
		Card c2 = cf.createCard(2, u2);
		
		Bike b1 = new ElectricBike();
		Bike b2 = new MechanicBike();
		Bike b3 = new MechanicBike();
		Bike b4 = new ElectricBike();
		Bike b5 = new ElectricBike();
		Bike b6 = new MechanicBike();
		Bike b7 = new MechanicBike();
		Bike b8 = new MechanicBike();
		
		s1.addSlot(5);
		s2.addSlot(3);
		s3.addSlot(2);
		
		Slot slot11 = s1.getParkingSlots().get(0);
		Slot slot12 = s1.getParkingSlots().get(1);
		Slot slot13 = s1.getParkingSlots().get(2);
		Slot slot14 = s1.getParkingSlots().get(3);
		Slot slot15 = s1.getParkingSlots().get(4);
		Slot slot21 = s2.getParkingSlots().get(0);
		Slot slot22 = s2.getParkingSlots().get(1);
		Slot slot23 = s2.getParkingSlots().get(2);
		Slot slot31 = s3.getParkingSlots().get(0);
		Slot slot32 = s3.getParkingSlots().get(1);
		
		
		LocalDateTime t1 = LocalDateTime.of(2019,03,26,14,56);
		LocalDateTime t2 = LocalDateTime.of(2019,03,26,15,30);
		LocalDateTime t3 = LocalDateTime.of(2019,03,26,15,57);
		LocalDateTime t4 = LocalDateTime.of(2019,03,26,16,32);
		LocalDateTime t5 = LocalDateTime.of(2019,03,26,16,45);
		LocalDateTime t1t2 = LocalDateTime.of(2019, 03, 26, 15, 00);
		LocalDateTime t6 = LocalDateTime.of(2019, 03, 26, 16, 50);
		LocalDateTime t7 = LocalDateTime.of(2019,  03, 26, 17, 40);
		LocalDateTime t8 = LocalDateTime.of(2019,  03, 26, 17, 50);
		LocalDateTime t9 = LocalDateTime.of(2019,  03, 26, 17, 59);
		LocalDateTime t10 = LocalDateTime.of(2019,  03, 26, 18, 11);
		
		assertAll("Renting of a Bike",
				() -> {
					slot11.setOnline(false, t1);
					slot12.setBike(b1, t1);
					slot13.setBike(b2, t1);
					slot14.setOnline(false,t1);
					
					slot21.setBike(b3, t1);
					slot22.setBike(b4, t1);
					
					
					assertAll("No ongoing ride",
							() -> assertNull(u1.getOngoingRide())
					); 
					
					s1.pickUpElectricBike(c1, t2); 
					
					assertAll("Manage to pick up bike, checking ride",
							() -> assertNotEquals(null, u1.getOngoingRide()),
							() -> assertEquals(b1, u1.getOngoingRide().getBike()),
							() -> assertEquals(c1, u1.getOngoingRide().getCard()),
							() -> assertEquals(t2, u1.getOngoingRide().getStartRide()),
							() -> assertEquals(null, u1.getOngoingRide().getEndRide()),
							() -> assertEquals(net, u1.getOngoingRide().getNet()),
							
							() -> {
								assertThrows(OngoingRideException.class,
										() -> {
											u1.getOngoingRide().getRideTime();
										}
								);
							}
					);	
					
					assertAll("Manage to pick up bike, checking station and slot",
							() -> assertEquals(null,slot12.getBike()),
							() -> assertEquals(1, s1.getTotalRents()),
							() -> assertEquals(1, s1.getTotalOperations()),
							() -> assertEquals(0, s1.getTotalReturns()),
							() -> assertEquals(2, slot12.getSlotHistory().size()),
							() -> assertEquals(t2, slot12.getSlotHistory().get(0).getEndTime()),
							() -> assertEquals(t2, slot12.getSlotHistory().get(1).getStartTime()),
							() -> assertEquals(null, slot12.getSlotHistory().get(1).getEndTime()),
							() -> assertEquals(slot13, s1.hasBikeAvailable()),
							() -> assertEquals(slot13, s1.hasMechanicBikeAvailable()),
							() -> assertEquals(null, s1.hasElectricBikeAvailable())
							
					);	
					 
					assertAll("Cannot pick up an other bike",
							() -> {
								assertThrows(OngoingRideException.class,
										() -> {
											s2.pickUpBike(c1, t3); 
										}
								);
							}	
					);
					

					
					s2.dropBike(c1,t3);
					
					assertAll ("Manage to drop bike, checking ride and rideHistory",
							() -> assertNull(u1.getOngoingRide()),
							() -> assertTrue(s2.isStationFull()),
							() -> assertNull(s2.availableSlot()),
							
							() -> {
								assertThrows(NoOngoingRideException.class,
										() -> {
											s2.dropBike(c1, t5);
										}
								);
							},
						
							() -> assertEquals(1, net.getRideHistory().size()),
							() -> assertEquals(t2, net.getRideHistory().get(0).getStartRide()),
							() -> assertEquals(t3, net.getRideHistory().get(0).getEndRide()),
							() -> assertEquals(b1, net.getRideHistory().get(0).getBike()),
							() -> assertEquals(27, net.getRideHistory().get(0).getRideTime()),
							() -> assertEquals(net, net.getRideHistory().get(0).getNet()),
							() -> assertEquals(1, s2.getTotalOperations())
						
					
					 ); 
					PathStrategy ps = new FastestPathStrategy(net);
					Itinerary i1 = u1.calculateItinerary(p5, p1, ps);
					
					assertAll("Compute itinerary 1",									
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(s1, i1.getEndStation()),
							() -> assertEquals(s2, i1.getStartStation())
							
					);
					

				}
		);
	}
	
	
	

}
