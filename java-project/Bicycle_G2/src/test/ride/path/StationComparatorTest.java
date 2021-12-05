/**
 * 
 */
package test.ride.path;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bike.Bike;
import bike.BikeFactory;
import bike.ElectricBike;
import bike.MechanicBike;
import ride.Network;
import ride.path.DistanceBasicComparator;
import ride.path.DistanceEndComparator;
import ride.path.DistanceStartComparator;
import station.Station;
import station.StationFactory;
import station.StationSamePositionException;
import station.TypeStationException;
import tools.Point;

/**
 * The test class for {@link ride.path.StationComparator}. It contains the tests for each subclass.
 *
 */
class StationComparatorTest {
	
	private Point point;
	private StationFactory fact;

	/**
	 * This method creates a network which is used in each test.
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		point = new Point(0, 0);
		fact = new StationFactory(new Network());
	}

	/**
	 * Test method for {@link ride.path.StationComparator#getDistanceDiff(station.Station, station.Station)}.
	 */
	@Test
	void testGetDistanceDiff() {
		DistanceBasicComparator dbc = new DistanceBasicComparator(point);
		assertAll("Many distance differences tests",
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 10));
				Station s2 = fact.createStation("Standard", new Point(0, 20));
				assertEquals(10, dbc.getDistanceDiff(s2, s1));
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(5, 0));
				Station s2 = fact.createStation("Standard", new Point(10, 0));
				assertEquals(5, dbc.getDistanceDiff(s2, s1));
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(10, 10));
				Station s2 = fact.createStation("Standard", new Point(20, 20));
				assertEquals(10*Math.sqrt(2), dbc.getDistanceDiff(s2, s1));
			}
		);
	}
	
	@Test
	void testDistanceBasicComparator() {
		DistanceBasicComparator dbc = new DistanceBasicComparator(point);
		assertAll(
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 10));
				Station s2 = fact.createStation("Standard", new Point(0, 20));
				assertTrue(dbc.compare(s1, s2) < 0);
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(10, 0));
				Station s2 = fact.createStation("Standard", new Point(0, -10));
				assertTrue(dbc.compare(s1, s2) == 0);
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 30));
				Station s2 = fact.createStation("Standard", new Point(20, 0));
				assertTrue(dbc.compare(s1, s2) > 0);
			}
		);
	}

	@Test
	void testDistanceEndComparator() throws TypeStationException, StationSamePositionException {
		DistanceEndComparator dec = new DistanceEndComparator(point);
		Station s1 = fact.createStation("Standard", new Point(0, 10));
		Station s2 = fact.createStation("Standard", new Point(0, 20));
		assertAll(
			() -> assertEquals(0, dec.compare(s1, s2), "Comparing two stations with no free slot"),
			() -> {
				s2.addSlot(2);
				assertTrue(dec.compare(s1, s2) > 0, "s1 closer but has no available slot");
			},
			() -> {
				s1.addSlot();
				assertTrue(dec.compare(s1, s2) < 0, "s1 closer and both have available slots");
			}
		);
	}
	
	@Test
	void testDistanceStartComparator() throws TypeStationException, StationSamePositionException {
		assertAll(
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 10));
				Station s2 = fact.createStation("Standard", new Point(0, 20));
				DistanceStartComparator dsc = new DistanceStartComparator(point);
				assertAll("Distance Start Comparator with no bikeType",
					() -> assertEquals(0, dsc.compare(s1, s2), "Comparing stations with no slot"),
					() -> {
						s1.addSlot();
						s2.addSlot();
						Bike b2 = new ElectricBike();
						s2.getParkingSlots().get(0).setBike(b2, LocalDateTime.of(2012, 12, 21, 22, 3));
						assertTrue(dsc.compare(s1, s2) > 0, "s1 is closer but has no bike available");
					},
					() -> {
						Bike b1 = new MechanicBike();
						s1.getParkingSlots().get(0).setBike(b1, LocalDateTime.of(2012, 12, 21, 22, 5));
						assertTrue(dsc.compare(s1, s2) < 0, "s1 is closer and both have bikes");
					}
				);
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 30));
				Station s2 = fact.createStation("Standard", new Point(0, 40));
				DistanceStartComparator dscE = new DistanceStartComparator(point, BikeFactory.ELECTRIC);
				assertAll("Distance Start Comparator with ElectricBike",
					() -> assertEquals(0, dscE.compare(s1, s2), "Comparing stations with no slot"),
					() -> {
						s1.addSlot();
						s2.addSlot();
						Bike b2 = new ElectricBike();
						s2.getParkingSlots().get(0).setBike(b2, LocalDateTime.of(2012, 12, 21, 22, 3));
						assertTrue(dscE.compare(s1, s2) > 0, "s1 is closer but has no bike available");
					},
					() -> {
						Bike b1 = new MechanicBike();
						s1.getParkingSlots().get(0).setBike(b1, LocalDateTime.of(2012, 12, 21, 22, 5));
						assertTrue(dscE.compare(s1, s2) > 0, "s1 is closer but has MechanicBike instead of ElectricBike");
					},
					() -> {
						Bike b1 = new ElectricBike();
						s1.addSlot();
						s1.getParkingSlots().get(1).setBike(b1, LocalDateTime.of(2012, 12, 21, 22, 5));
						assertTrue(dscE.compare(s1, s2) < 0, "s1 is closer and both have ElectricBike");
					}
				);
			},
			() -> {
				Station s1 = fact.createStation("Standard", new Point(0, 50));
				Station s2 = fact.createStation("Standard", new Point(0, 60));
				DistanceStartComparator dscM = new DistanceStartComparator(point, BikeFactory.MECHANIC);
				assertAll("Distance Start Comparator with ElectricBike",
					() -> assertEquals(0, dscM.compare(s1, s2), "Comparing stations with no slot"),
					() -> {
						s1.addSlot();
						s2.addSlot();
						Bike b2 = new MechanicBike();
						s2.getParkingSlots().get(0).setBike(b2, LocalDateTime.of(2012, 12, 21, 22, 3));
						assertTrue(dscM.compare(s1, s2) > 0, "s1 is closer but has no bike available");
					},
					() -> {
						Bike b1 = new ElectricBike();
						s1.getParkingSlots().get(0).setBike(b1, LocalDateTime.of(2012, 12, 21, 22, 5));
						assertTrue(dscM.compare(s1, s2) > 0, "s1 is closer but has ElectricBike instead of MechanicBike");
					},
					() -> {
						Bike b1 = new MechanicBike();
						s1.addSlot();
						s1.getParkingSlots().get(1).setBike(b1, LocalDateTime.of(2012, 12, 21, 22, 5));
						assertTrue(dscM.compare(s1, s2) < 0, "s1 is closer and both have MechanicBike");
					}
				);
			}
		);
	}
}
