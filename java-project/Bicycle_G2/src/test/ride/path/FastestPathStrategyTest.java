package test.ride.path;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bike.BikeFactory;
import bike.ElectricBike;
import bike.MechanicBike;
import ride.Network;
import ride.path.FastestPathStrategy;
import station.Station;
import station.StationFactory;
import tools.Point;

class FastestPathStrategyTest {
	
	static Network net;
	static LocalDateTime changeTime = LocalDateTime.of(2019, 1, 1, 0, 0);
	static FastestPathStrategy fps;
	static Point start;
	static Point destination;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		start = new Point(0,0);
		destination = new Point(0,110);
	}

	@BeforeEach
	void setUp() throws Exception {
		net = new Network();
		fps = new FastestPathStrategy(net);
		StationFactory sf = new StationFactory(net);
		for (int i = 1; i <= 5; i++) {
			sf.createStation("Standard", new Point(0, 20*i));
			sf.createStation("Standard", new Point(0, 20*i+1));
			net.getStations().get(2*i-2).addSlot();
			net.getStations().get(2*i-1).addSlot();
		}
	}

	@Test
	void testFindPathElectricBike() {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new ElectricBike(), changeTime);
				net.getStations().get(0).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is closer than station 1 but has no ElectricBike");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new ElectricBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is the closest and has ElectricBike");
			},
			() -> {
				net.getStations().get(9).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(8)};
				assertArrayEquals(expectedStations, stations, "Station 9 is the closest to the destination but has no free slot");
			}
		);
	}
	
	@Test
	void testFindPathMechanicBike() {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new MechanicBike(), changeTime);
				net.getStations().get(0).availableSlot().setBike(new ElectricBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is closer than station 1 but has no MechanicBike");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new MechanicBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is the closest and has MechanicBike");
			},
			() -> {
				net.getStations().get(9).availableSlot().setBike(new ElectricBike(), changeTime);
				Station[] stations = fps.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(8)};
				assertArrayEquals(expectedStations, stations, "Station 9 is the closest to the destination but has no free slot");
			}
		);
	}

	@Test
	void testFindPathBike() {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new ElectricBike(), changeTime);
				net.getStations().get(0).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = fps.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is closer than station 1 but ElectricBike is faster");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new ElectricBike(), changeTime);
				Station[] stations = fps.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(9)};
				assertArrayEquals(expectedStations, stations, "Station 0 is the closest and has ElectricBike");
			},
			() -> {
				net.getStations().get(9).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = fps.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(8)};
				assertArrayEquals(expectedStations, stations, "Station 9 is the closer to the destination but has no free slot");
			}
		);
	}

}
