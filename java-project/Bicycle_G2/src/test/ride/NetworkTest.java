package test.ride;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import card.Card;
import card.CreditCard;
import card.VlibreCard;
import card.VmaxCard;
import ride.Network;
import ride.Ride;
import station.PlusStation;
import station.StandardStation;
import station.Station;
import tools.Point;
import user.User;

class NetworkTest {

	@Test
	void testNetwork() {
		Network net = new Network();
		assertAll("Creation of the network",
			() -> assertNotNull(net),
			() -> assertEquals(new ArrayList<Station>(), net.getStations(), "net.stations has to be a empty arraylist"),
			() -> assertEquals(new ArrayList<Ride>(), net.getRideHistory(), "net.getRideHistory has to be a empty arraylist")
		);
	}

	@Test
	void testNetworkString() {
		Network net = new Network("VLib");
		assertAll("Creation and name of the network",
			() -> assertNotNull(net),
			() -> assertEquals("VLib", net.getName(), "Name of the network"),
			() -> assertEquals(new ArrayList<Station>(), net.getStations(), "net.getStations() has to be a empty arraylist"),
			() -> assertEquals(new ArrayList<Ride>(), net.getRideHistory(), "net.getRideHistory has to be a empty arraylist")
		);
	}

	@Test
	void testAddStation() {
		Network net = new Network();
		assertAll(
			() -> {
				Station s = new StandardStation(new Point(0, 0), net);
				net.addStation(s);
				assertEquals(1, net.getStations().size(), "1 station");
			},
			() -> {
				Station s = new PlusStation(new Point(0, 0), net);
				net.addStation(s);
				assertEquals(2, net.getStations().size(), "2 stations");
			}
		);
	}

	@Test
	void testRemoveStation() {
		Network net = new Network();
		Station s1 = new StandardStation(new Point(0, 1), net);
		Station s2 = new PlusStation(new Point(1, 1), net);
		Station s3 = new StandardStation(new Point(1, 0), net);
		net.addStation(s1);
		net.addStation(s2);
		net.addStation(s3);
		assertAll(
			() -> {
				net.removeStation(s1);
				ArrayList<Station> stations = new ArrayList<Station>();
				stations.add(s2);
				stations.add(s3);
				assertEquals(stations, net.getStations(), "s1 removed");
			},
			() -> {
				net.removeStation(s2);
				ArrayList<Station> stations = new ArrayList<Station>();
				stations.add(s3);
				assertEquals(stations, net.getStations(), "s1 and s2 removed");
			}
		);
	}

	@Test
	void testAddCard() {
		Network net = new Network();
		assertAll(
			() -> {
				User u = new User("Pietro");
				Card c = new CreditCard(u);
				net.addCard(c);
				assertEquals(1, net.getCards().size(), "1 card");
			},
			() -> {
				User u = new User("Chloe");
				Card c = new VlibreCard(u);
				net.addCard(c);
				assertEquals(2, net.getCards().size(), "2 cards");
			},
			() -> {
				User u = new User("Paolo");
				Card c = new VmaxCard(u);
				net.addCard(c);
				assertEquals(3, net.getCards().size(), "3 cards");
			}
		);
	}

	@Test
	void testRemoveCard() {
		Network net = new Network();
		Card c1 = new CreditCard(new User("Pietro"));
		Card c2 = new VlibreCard(new User("Chloe"));
		Card c3 = new VmaxCard(new User("Paolo"));
		net.addCard(c1);
		net.addCard(c2);
		net.addCard(c3);
		assertAll(
			() -> {
				net.removeCard(c1);
				ArrayList<Card> cards = new ArrayList<Card>();
				cards.add(c2);
				cards.add(c3);
				assertEquals(cards, net.getCards(), "c1 removed");
			},
			() -> {
				net.removeCard(c3);
				ArrayList<Card> cards = new ArrayList<Card>();
				cards.add(c2);
				assertEquals(cards, net.getCards(), "c1 and c3 removed");
			}
		);
	}


}
