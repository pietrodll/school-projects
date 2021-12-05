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
import ride.path.MinimalWalkingStrategy;
import station.Station;
import station.StationFactory;
import tools.Point;

class MinimalWalkingTest {
	
	private static Network net;
	private static LocalDateTime changeTime = LocalDateTime.of(2019, 1, 1, 0, 0);
	private static MinimalWalkingStrategy mws;
	private static Point start;
	private static Point destination;
	
	@BeforeAll
	static void setUpAll() {
		start = new Point(0,0);
		destination = new Point(0,60);
	}

	@BeforeEach
	void setUp() throws Exception {
		net = new Network();
		mws = new MinimalWalkingStrategy(net);
		StationFactory sf = new StationFactory(net);
		for (int i = 1; i <= 5; i++) {
			sf.createStation("Standard", new Point(0, 10*i));
			net.getStations().get(i-1).addSlot();
		}
	}

	@Test
	void testFindPathElectricBike() throws Exception {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new ElectricBike(), changeTime);
				net.getStations().get(0).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is closer than station 2 but has no ElectricBike");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new ElectricBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is the closest and has ElectricBike");
			},
			() -> {
				net.getStations().get(4).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.ELECTRIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(3)};
				assertArrayEquals(expectedStations, stations, "Station 5 is the closer to the destination but has no free slot");
			}
		);
	}
	
	@Test
	void testFindPathMechanicBike() throws Exception {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new MechanicBike(), changeTime);
				net.getStations().get(0).availableSlot().setBike(new ElectricBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is closer than station 2 but has no MechanicBike");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new MechanicBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is the closest and has MechanicBike");
			},
			() -> {
				net.getStations().get(4).availableSlot().setBike(new ElectricBike(), changeTime);
				Station[] stations = mws.findPath(start, destination, BikeFactory.MECHANIC);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(3)};
				assertArrayEquals(expectedStations, stations, "Station 5 is the closer to the destination but has no free slot");
			}
		);
	}
	
	@Test
	void testFindPathBike() throws Exception {
		assertAll(
			() -> {
				net.getStations().get(1).availableSlot().setBike(new MechanicBike(), changeTime);
				Station[] stations = mws.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(1), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is closer than station 2 but has no Bike");
			},
			() -> {
				net.getStations().get(0).getParkingSlots().get(0).setBike(new MechanicBike(), changeTime);
				Station[] stations = mws.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(4)};
				assertArrayEquals(expectedStations, stations, "Station 1 is the closest and has Bike");
			},
			() -> {
				net.getStations().get(4).availableSlot().setBike(new ElectricBike(), changeTime);
				Station[] stations = mws.findPath(start, destination);
				Station[] expectedStations = {net.getStations().get(0), net.getStations().get(3)};
				assertArrayEquals(expectedStations, stations, "Station 5 is the closer to the destination but has no free slot");
			}
		);
	}

}
