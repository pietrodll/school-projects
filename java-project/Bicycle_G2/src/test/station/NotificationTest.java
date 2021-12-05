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

public class NotificationTest {
	
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
					
					
					PathStrategy ps = new FastestPathStrategy(net);
					Itinerary i1 = u1.calculateItinerary(p5, p1, ps);
					
					assertAll("Compute itinerary 1",									
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(s1, i1.getEndStation()),
							() -> assertEquals(s2, i1.getStartStation())	
					);
					
					u1.setItinerary(i1);
					assertAll("Follow itinerary",
							() -> assertNotNull(u1.getItinerary())	
					);
					
				
					assertAll("Observer Pattern : User added to observers of the Station",
							() -> assertEquals(1, s1.getObservers().size()),
							() -> assertEquals(u1, s1.getObservers().get(0))
					);
					
					assertAll("Observer Pattern : Notification if Station goes offline but no ongoing ride so no possibility to recalculate itinerary ",
							() -> assertTrue(s1.isOnline()),
							() -> assertNull(u1.getOngoingRide()),
							() -> {
								s1.setOnline(false);
								System.out.println("case 1 : No automatic possibility to recalculate itinerary");
							},
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(0, s1.getObservers().size())
					);
							
					s1.setOnline(true);
					u1.setItinerary(i1);
					assertAll("Observer Pattern : Notification if no Slots available but no ongoing Ride so no possibility to recalculate itinerary",
							() -> assertNotNull(u1.getItinerary()),
							() -> assertEquals(1, s1.getObservers().size()),
							() -> assertTrue (s1.isOnline()),
							() -> assertFalse(s1.isStationFull()),
							() -> {
								slot15.setBike(b6, t5);
								slot12.setOnline(false, t6);
								System.out.println("case 2 : No automatic possibility to recalculate itinerary");	
							},
							() -> assertTrue(s1.isStationFull()),
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(0, s1.getObservers().size())
					); 
					
					Itinerary i2 = u1.calculateItinerary(p5, p3, ps);
					
					assertAll("Compute itinerary 2",									
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(s3, i2.getEndStation()),
							() -> assertEquals(s2, i2.getStartStation())
					);
					
					/* !!! TO BE READ !!!
					 * 
					 * The two following assertAll ObserverPattern ask the client to write into the console. They cannot work simultaneously. 
					 * If you want to test one, you have to comment the other.
					 */
					
					
					assertAll("Observer Pattern : Notification if station goes offline with ongoing ride, case 1",
							() -> {
								u1.setItinerary(i2);
								s2.pickUpBike(c1, t7);
							},
							
							() -> assertEquals(1, s3.getObservers().size()),
							() -> assertEquals(u1, s3.getObservers().get(0)),
							
							() -> {
								System.out.println("---------------");
								System.out.println("case 1 : CONSOLE answer 'yes' to the question");
								s3.setOnline(false);
								},
							() -> assertNotNull(u1.getItinerary()),
							() -> assertNotEquals(s3, u1.getItinerary().getEndStation()),
							() -> assertEquals (s2, u1.getItinerary().getEndStation()),
							() -> assertEquals(0, s3.getObservers().size()),
							() -> assertEquals(1, s2.getObservers().size())
					);				
					
					/*
					assertAll("Observer Pattern : Notification if station goes offline with ongoing ride, case 2",
							() -> {
								u1.setItinerary(i2);
								s2.pickUpBike(c1, t7);
								s3.setOnline(true);
								s3.dropBike(c1, t8);
							},
							
							() -> assertNull(u1.getOngoingRide()),
							
							() -> {
								u1.setItinerary(i2);
								s2.pickUpBike(c1, t9);
							},
							
							() -> assertNotNull(u1.getOngoingRide()),
							() -> assertNotNull(u1.getItinerary()),
							() -> assertEquals(1, s3.getObservers().size()),
							() -> assertEquals(u1, s3.getObservers().get(0)),
						
							() -> {
								System.out.println("-------------");
								System.out.println("case 2 : CONSOLE do not answer 'yes' to the question");
								s3.setOnline(false);
								},
							() -> assertNull(u1.getItinerary()),
							() -> assertEquals(0, s3.getObservers().size())
					);
					*/
						
					
					
					
					
					
				}
		);
	}
	
	
	

}
