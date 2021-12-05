package test.station;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bike.Bike;
import bike.ElectricBike;
import bike.MechanicBike;
import card.*;
import ride.Network;
import station.*;
import tools.Point;
import user.User;

public class StationTest {
	
	private Network net;
	private StationFactory sf;
	private CardFactory cf;
	private User u1;
	private User u2;
	
	@BeforeEach
	void setUp() {
		net = new Network();
		sf = new StationFactory(net);
		cf = new CardFactory(net);
		u1 = new User ("DellinoPietro");
		u2 = new User ("GentgenChloé");
	}
	
	@Test
	void testDifferentIdStation() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point (1,3);
		Point p2 = new Point (2,3);
		Point p3 = new Point (4,9);
		
		Station s1 = sf.createStation("Standard", p1);
		Station s2 = sf.createStation("Standard", p2);
		Station s3 = sf.createStation("Plus", p3);
		assertAll(
				() -> assertEquals(s1.getId()+1, s2.getId()),
				() -> assertEquals(s1.getId()+2, s3.getId())
		);				
	}
				
	@Test 
	void testSamePositionStation() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point(3, 10);
		sf.createStation("Standard", p1);
		assertThrows(StationSamePositionException.class,
				() -> {
					sf.createStation("Standard", p1);
				}
		);
		
	}
	
	@Test
	void testIfNoSlotThenStationFull() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point(3, 10);
		Station s1 = sf.createStation("Standard", p1);
		assertTrue(s1.isStationFull());
	}
	
	@Test
	void testIfNoSlotThenSlotAvailableNull() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point(3, 10);
		Station s1 = sf.createStation("Standard", p1);
		assertEquals(null, s1.availableSlot());
	}
	
	@Test
	void testCardIdentification() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point(3, 10);
		Station s1 = sf.createStation("Standard", p1);
		Card c1 = cf.createCard(1,u1);
		Card c2 = cf.createCard(2, u2);
		assertEquals(u1, s1.identifyUser(c1));
		assertEquals(u2, s1.identifyUser(c2));
	}	
	
	@Test
	void testSlotandState() throws TypeStationException, StationSamePositionException {
		Point p1 = new Point(3, 10);
		Point p2 = new Point (60, 4);
		Station s1 = sf.createStation("Standard", p1);
		Station s2 = sf.createStation("Standard", p2);
		Bike b1 = new ElectricBike();
		Bike b2 = new MechanicBike();
		Bike b3 = new MechanicBike();
	
		s1.addSlot();
		s2.addSlot();
		s1.addSlot();
		s1.addSlot();
		s2.addSlot();
		assertAll("Creating Stations and Slots",
				() -> {
					assertAll("Number of Slots in Stations",
							() -> assertEquals(3, s1.getParkingSlots().size(), "Number of Slots in S1"),
							() -> assertEquals(2, s2.getParkingSlots().size(), "Number of Slots in S2")
				
					);	
		
				},
				() -> {
					int idS1 = s1.getId();
					int idS2 = s2.getId();
					Slot slot11 = s1.getParkingSlots().get(0);
					Slot slot12 = s1.getParkingSlots().get(1);
					Slot slot13 = s1.getParkingSlots().get(2);
					Slot slot21 = s2.getParkingSlots().get(0);
					LocalDateTime t1 = LocalDateTime.of(2019,03,26,14,56);
					LocalDateTime t2 = LocalDateTime.of(2019,03,26,15,30);
					LocalDateTime t3 = LocalDateTime.of(2019,03,26,15,57);
					LocalDateTime t4 = LocalDateTime.of(2019,03,26,16,32);
					LocalDateTime t5 = LocalDateTime.of(2019,03,26,16,45);
					slot11.setBike(b1,t1);
					slot11.setBike(null, t2);
					slot11.setOnline(false, t3);
					slot11.setOnline(true, t4);
					slot11.setBike(b2, t5);
					slot12.setOnline(false, t1);
					slot12.setOnline(true, t3);
					slot13.setOnline(false,t1);
					slot13.setBike(b3, t2);
					slot13.setOnline(true, t3);
					slot13.setBike(null, t4);
					slot13.setOnline(false, t5);
					
					SlotState slotstate111 = slot11.getSlotHistory().get(0);
					SlotState slotstate112 = slot11.getSlotHistory().get(1);
					SlotState slotstate113 = slot11.getSlotHistory().get(2);
					SlotState slotstate114 = slot11.getSlotHistory().get(3);
					SlotState slotstate115 = slot11.getSlotHistory().get(4);
					
					SlotState slotstate131 = slot13.getSlotHistory().get(0);
					SlotState slotstate132 = slot13.getSlotHistory().get(1);
					SlotState slotstate133 = slot13.getSlotHistory().get(2);
					SlotState slotstate134 = slot13.getSlotHistory().get(3);
					
					
					assertAll("Station and Slots",
					() -> {
						assertAll("ID Station and Slots",
								() -> assertNotEquals(idS1, idS2, "Different ID for station"),
								() -> assertEquals(idS1+1, idS2, "Succesive ID for succesive stations"),
								() -> assertEquals(idS1*1000, slot11.getId(), "Correct ID for First Slot of S1" ),
								() -> assertEquals(idS1*1000 + 1, slot12.getId(), "Correct ID for Second Slot of S2"),
								() -> assertEquals(idS2*1000, slot21.getId(), "Correct ID for First Slot of S2")
						);
					},
					() -> {
						assertAll("Correct Slot State for Slot 1 of S1 between t1 and t6",
								() -> assertEquals(t1, slotstate111.getStartTime(), "1"),
								() -> assertEquals(t2, slotstate111.getEndTime(), "2"),
								() -> assertEquals(true, slotstate111.isOnline(), "3"),
								() -> assertEquals(b1, slotstate111.getBike(), "4"),
								() -> assertEquals(t2, slotstate112.getStartTime(), "5"),
								() -> assertEquals(t3, slotstate112.getEndTime(), "6"),
								() -> assertEquals(true, slotstate112.isOnline(), "7"),
								() -> assertEquals(null, slotstate112.getBike(), "8"),
								() -> assertEquals(t3, slotstate113.getStartTime(), "9"),
								() -> assertEquals(t4, slotstate113.getEndTime(), "10"),
								() -> assertEquals(false, slotstate113.isOnline(), "11"),
								() -> assertEquals(null, slotstate113.getBike(), "12"),
								() -> assertEquals(t4, slotstate114.getStartTime(), "13"),
								() -> assertEquals(t5, slotstate114.getEndTime(), "14"),
								() -> assertEquals(true, slotstate114.isOnline(), "15"),
								() -> assertEquals(null, slotstate114.getBike(), "16"),
								() -> assertEquals(t5, slotstate115.getStartTime(), "17"),
								() -> assertEquals(true, slotstate115.isOnline(), "18"),
								() -> assertEquals(b2, slotstate115.getBike(), "19"),
								() -> assertEquals(null, slotstate115.getEndTime(), "20")
						);
					},
					() -> {
						assertAll("Stations methods isStationFull/availableSlot/hasBikeAvailable",
								() -> assertFalse(s1.isStationFull()),
								() -> assertEquals(slot12, s1.availableSlot()),
								() -> assertEquals(slot11, s1.hasBikeAvailable()),
								() -> assertNull(s1.hasElectricBikeAvailable()),
								() -> assertEquals(slot11, s1.hasMechanicBikeAvailable()),
								() -> assertFalse(b2 instanceof ElectricBike),
								() -> assertTrue(b2 instanceof Bike), 
								() -> assertTrue(b2 instanceof MechanicBike),
								() -> assertEquals(b2, slot11.getBike()),
								() -> assertEquals(null, slot12.getBike()), 
								() -> assertEquals(null, slot13.getBike())
								
						);
					},
					() -> {
						assertAll("Slot method isOccupied and isOnline",
								() -> assertEquals(true, slot11.getisOccupied(), "Slot 1 is Online and has a Bike"),
								() -> assertEquals(false, slot12.getisOccupied(), "Slot 2 is Online without a Bike"),
								() -> assertEquals(true, slot13.getisOccupied(), "Slot 3 is Offline and without a Bike"),
								
								() -> assertTrue(slot11.isOnline()),
								() -> assertTrue(slot12.isOnline()),
								() -> assertFalse(slot13.isOnline())
							);
					},
					() -> {
						assertAll("Slot State method isOccupied",
								() -> assertEquals(true, slotstate111.getisOccupied(), "State 1 of Slot 11"),
								() -> assertEquals(false, slotstate112.getisOccupied(), "State 2 of Slot 11"),
								() -> assertEquals(true, slotstate113.getisOccupied(), "State 3 of Slot 11"),
								() -> assertEquals(false, slotstate114.getisOccupied(), "State 4 of Slot 11"),
								() -> assertEquals(true, slotstate131.getisOccupied(), "State 1 of Slot 13"),
								() -> assertEquals(true, slotstate132.getisOccupied(), "State 2 of Slot 13"),
								() -> assertEquals(true, slotstate133.getisOccupied(), "State 3 of Slot 13"),
								() -> assertEquals(false, slotstate134.getisOccupied(), "State 4 of Slot 13")
								
						);
					},
					() -> {
						
						LocalDateTime t10 = LocalDateTime.of(2019,03,26,16,30);
						LocalDateTime t11 = LocalDateTime.of(2019,03,25,16,30);
						LocalDateTime t1t2 = LocalDateTime.of(2019, 03, 26, 15, 00);
						LocalDateTime t4t5 = LocalDateTime.of(2019,  03, 26, 16, 40);
						LocalDateTime t6 = LocalDateTime.of(2019, 03, 26, 16, 50);
						
						assertAll("Computing Occupation Rate and all associated methods",
								() -> {
									assertAll("IndexSlotState method for Slot 11",
											() -> {
												assertThrows(NoSlotStateAtDateException.class, 
														() -> {
															slot11.indexSlotState(t11);	
														},"If date is before creation of a slot, then throws Exception"
													);
											},
											() -> assertEquals(2, slot11.indexSlotState(t10), "If date is during a Slot State"),
											() -> assertEquals(2, slot11.indexSlotState(t3), "If date is at a change of Slot State"),
											() -> assertEquals (4, slot11.indexSlotState(t6), "If date is during the current Slot State")
											
										);
								},
								() -> {
									assertAll("ComputeOccupationsTime for Slot 11",
											() -> assertEquals (69,slot11.computeOccupationTime(t1t2, t4t5),"If start time and end time are during finished Slot States"),
											() -> assertEquals(35, slot11.computeOccupationTime(t2,t4t5), "If start time is a junction time and end time is during Slot State"),
											() -> assertEquals(35, slot11.computeOccupationTime(t3, t5), "If start time and end time are junction times"),
											() -> assertEquals(35, slot11.computeOccupationTime(t3, t4), "If start time and end time are the start and the end of the same Slot State"),
											
											() -> assertEquals(69, slot11.computeOccupationTime(t11,t5), "Start time is before creation of the Slot, the exception is handled and the time is computed from the begginning of the last Slot"),
											() -> assertEquals(74, slot11.computeOccupationTime(t1, t6), "End time is during the current slot state, the time of the last Slot State is computed until the end of observation")
										);
								},
								() -> {
									assertAll("ComputeOccupationsTime for Slot 13",
											() -> assertEquals(96, slot13.computeOccupationTime(t1,t5)),
											() -> assertEquals(62, slot13.computeOccupationTime(t2,t5)), 
											() -> assertEquals(96, slot13.computeOccupationTime(t1t2,t4t5))
									);
								},
								
								() -> {
									assertAll("getRateOccupation for Station 1",
											() -> assertEquals((96+61+69)/(3*109), s1.getRateOccupation(t1, t5)),
											() -> assertEquals((96+61+69)/(3*109), s1.getRateOccupation(t1t2, t4t5)),
											() -> assertEquals((96+61+69+5)/(3*109), s1.getRateOccupation(t1, t6), "If end time is during the current slot slate"), 
											() -> assertEquals((96+61+69-34*3)/(3*109), s1.getRateOccupation(t2, t4t5))
									);
								}
							);
					}
				);
		
				}
		);
	
	}

}


