package test.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import card.Card;
import card.CardFactory;
import card.VlibreCard;
import card.VmaxCard;
import controller.ExistingNameException;
import controller.InexistingNetworkNameException;
import controller.InexistingStationIdException;
import controller.NetworkManager;
import ride.Network;
import station.Station;
import tools.Point;

class NetworkManagerTest {
	
	@Test
	void testNetworkManager() {
		NetworkManager nm = new NetworkManager();
		assertAll(
			() -> assertNotNull(nm),
			() -> assertNotNull(nm.getNetworks())
		);
	}

	@Test
	void testSetupNetworkString() {
		NetworkManager nm = new NetworkManager();
		assertAll(
			() -> assertNotNull(nm.setupNetwork("Network"), "Creation of the network"),
			() -> {
				Network net = nm.setupNetwork("VLib");
				ArrayList<Station> stations = net.getStations();
				assertAll("Properties of the network",
					() -> assertEquals(10, net.getStations().size()),
					() -> {
						for (int i = 0; i < stations.size(); i++) {
							assertEquals(10, stations.get(i).getParkingSlots().size(), "Number of slots of station number " + i);
						}
					},
					() -> {
						for (int i = 0; i < stations.size(); i++) {
							Station s = stations.get(i);
							String is = ((Integer) i).toString();
							assertAll(
								() -> assertTrue(0 <= s.getP().getX() && s.getP().getX() < 4, "X position of station number " + is),
								() -> assertTrue(0 <= s.getP().getY() && s.getP().getY() < 4, "Y position of station number " + is)
							);
						}
					}
				);
			},
			() -> assertThrows(ExistingNameException.class, () -> { nm.setupNetwork("VLib"); })
		);
	}
	

	@Test
	void testSetupNetworkStringIntIntDoubleInt() {
		NetworkManager nm = new NetworkManager();
		assertAll(
			() -> assertNotNull(nm.setupNetwork("Network"), "Creation of the network"),
			() -> {
				Network net = nm.setupNetwork("VLib", 20, 11, 7.5, 150);
				ArrayList<Station> stations = net.getStations();
				assertAll("Properties of the network",
					() -> assertEquals(20, net.getStations().size()),
					() -> {
						for (int i = 0; i < stations.size(); i++) {
							assertEquals(11, stations.get(i).getParkingSlots().size(), "Number of slots of station number " + i);
						}
					},
					() -> {
						for (int i = 0; i < stations.size(); i++) {
							Station s = stations.get(i);
							String is = ((Integer) i).toString();
							assertAll(
								() -> assertTrue(0 <= s.getP().getX() && s.getP().getX() < 7.5, "X position of station number " + is),
								() -> assertTrue(0 <= s.getP().getY() && s.getP().getY() < 7.5, "Y position of station number " + is)
							);
						}
					}
				);
			},
			() -> assertThrows(ExistingNameException.class, () -> { nm.setupNetwork("VLib"); }),
			() -> assertThrows(ExistingNameException.class, () -> { nm.setupNetwork("VLib", 12, 15, 7.1, 3); })
		);
	}
	

	@Test
	void testAddUser() {
		NetworkManager nm = new NetworkManager();
		assertAll(
			() -> {
				Network net = nm.setupNetwork("VLib");
				nm.addUser("Pietro", CardFactory.VLIBRE, "VLib");
				ArrayList<Card> cards = net.getCards();
				assertAll("Check user added",
					() -> assertEquals(1, cards.size(), "Check the number of cards on the network"),
					() -> assertEquals("Pietro", cards.get(0).getUser().getUserName(), "Check the name of the user"),
					() -> assertTrue(cards.get(0) instanceof VlibreCard, "Check the type of card")
				);
			},
			() -> {
				Network net = nm.findNetworkByName("VLib");
				nm.addUser("Chloe", CardFactory.VMAX, "VLib");
				ArrayList<Card> cards = net.getCards();
				assertAll("Check second user added",
					() -> assertEquals(2, cards.size(), "Check the number of cards on the network"),
					() -> assertEquals("Pietro", cards.get(0).getUser().getUserName(), "Check the name of the first user"),
					() -> assertTrue(cards.get(0) instanceof VlibreCard, "Check the type of the first card"),
					() -> assertEquals("Chloe", cards.get(1).getUser().getUserName(), "Check the name of the second user"),
					() -> assertTrue(cards.get(1) instanceof VmaxCard, "Check the type of the second card")
				);
			},
			() -> assertThrows(InexistingNetworkNameException.class, () -> { nm.addUser("Chloe", CardFactory.VMAX, "Network"); })
		);
	}
	

	@Test
	void testSetStationOffline() throws ExistingNameException {
		NetworkManager nm = new NetworkManager();
		Network net = nm.setupNetwork("Vlib");
		Station s = net.getStations().get(1);
		int id = s.getId();
		assertAll(
			() -> {
				nm.setStationOffline("Vlib", id);
				assertFalse(s.isOnline());
			},
			() -> assertThrows(InexistingNetworkNameException.class, () -> { nm.setStationOffline("Network", id); }),
			() -> assertThrows(InexistingStationIdException.class, () -> { nm.setStationOffline("Vlib", 250); })
		);
	}
	

	@Test
	void testSetStationOnline() throws ExistingNameException {
		NetworkManager nm = new NetworkManager();
		Network net = nm.setupNetwork("Vlib");
		Station s = net.getStations().get(1);
		int id = s.getId();
		assertAll(
			() -> {
				s.setOnline(false);
				assertFalse(s.isOnline());
				nm.setStationOnline("Vlib", id);
				assertTrue(s.isOnline());
			},
			() -> assertThrows(InexistingNetworkNameException.class, () -> { nm.setStationOnline("Network", id); }),
			() -> assertThrows(InexistingStationIdException.class, () -> { nm.setStationOnline("Vlib", 250); })
		);
	}
	
	
	@Test
	void testGetPointDistribution() {
		NetworkManager nm = new NetworkManager();
		Point[] points = nm.getPointDistribution(10, 4);
		assertAll(
			() -> assertNotNull(points),
			() -> {
				for (int i = 0; i < 10; i++) {
					assertTrue(points[i].getX() < 4 && points[i].getX() >= 0, "X position of point number " + i);
					assertTrue(points[i].getY() < 4 && points[i].getY() >= 0, "Y position of point number " + i);
				}
			},
			() -> {
				for (int i = 0; i < 9; i++) {
					for (int j = i + 1; j < 10; j++) {
						assertNotEquals(points[i], points[j]);
					}
				}
			}
		);
	}

	@Test
	void testFindStationByID() throws ExistingNameException {
		NetworkManager nm = new NetworkManager();
		Network net = nm.setupNetwork("Vlib");
		assertAll(
			() -> {
				Station s = net.getStations().get(5);
				assertSame(s, nm.findStationByID(s.getId(), net), "Checking the IDs");
			},
			() -> assertThrows(InexistingStationIdException.class, () -> { nm.findStationByID(250, net); })
		);
	}
	
	@Test
	void testFindNetworkByName() throws ExistingNameException {
		NetworkManager nm = new NetworkManager();
		Network net1 = nm.setupNetwork("Velib1");
		Network net2 = nm.setupNetwork("Velib2");
		assertAll(
			() -> assertSame(net1, nm.findNetworkByName("Velib1")),
			() -> assertSame(net2, nm.findNetworkByName("Velib2")),
			() -> assertThrows(InexistingNetworkNameException.class, () -> { nm.findNetworkByName("Velib3"); })
		);
	}
}
