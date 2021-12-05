/**
 * 
 */
package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import bike.Bike;
import bike.BikeFactory;
import card.Card;
import card.CardFactory;
import ride.Network;
import sorting.station.SortingStrategy;
import station.NoBikeAvailableException;
import station.NoElectricBikeAvailableException;
import station.NoMechanicBikeAvailableException;
import station.NoOngoingRideException;
import station.NoSlotAvailableException;
import station.OngoingRideException;
import station.Slot;
import station.Station;
import station.StationFactory;
import station.StationOfflineException;
import station.StationSamePositionException;
import station.TypeStationException;
import tools.NegativeTimeException;
import tools.NullDateException;
import tools.Point;
import user.User;

/**
 * This class provides the methods to set up a network and manage it, which is useful in both CLUI and GUI.
 * @author Pietro Dellino
 *
 */
public class NetworkManager {
	
	public static final LocalDateTime CREATION_DATE = LocalDateTime.of(2019, 1, 1, 0, 0);
	public static final LocalDateTime ADDING_DATE = LocalDateTime.of(2019, 1, 1, 0, 1);

	private ArrayList<Network> networks;
	
	public NetworkManager() { this.networks = new ArrayList<Network>(); }
	
	public ArrayList<Network> getNetworks() { return this.networks; }
	
	/**
	 * This method returns a {@code Network} object with 10 stations placed in a square area of size 4km, each station has 10 slots and the occupation rate of the network is 75%.
	 * @param name ({@code String}) The name of the network
	 * @return a {@code Network}
	 * @throws ExistingNameException 
	 * @see #setupNetwork(String, int, int, double, int)
	 */
	public Network setupNetwork(String name) throws ExistingNameException {
		Network net = this.setupNetwork(name, 10, 10, 4, 75);
		return net;
	}
	
	/**
	 * This method sets up a network according to the parameters. The stations are placed according to {@code getPointDistribution}. 
	 * @param name The name of the network
	 * @param nStation The number of stations
	 * @param nSlot The number of slots of each station
	 * @param s The side of the square area to place the stations
	 * @param nBikes The total number of bikes
	 * @return a network
	 * @throws ExistingNameException 
	 * @see #getPointDistribution(int, double)
	 */
	public Network setupNetwork(String name, int nStation, int nSlot, double s, int nBikes) throws ExistingNameException {
		Network net = new Network(name);
		for (Network n : this.networks) {
			if (net.getName().equals(n.getName())) {
				throw new ExistingNameException(name);
			}
		}
		this.networks.add(net);
		Random rand = new Random();
		StationFactory sf = new StationFactory(net);
		Point[] points = this.getPointDistribution(nStation, s);
		for (int i = 0; i < nStation; i++) {
			Station station = null;
			try {
				station = sf.createStation("Standard", points[i]);
			} catch (TypeStationException e) {
				e.printStackTrace();
			} catch (StationSamePositionException e) {
				System.out.println("According to the point distribution function, this should not happen");
				e.printStackTrace();
			}
			station.addSlot(nSlot);
		}
		for (int i = 0; i < nBikes; i++) {
			Station randomStation = net.getStations().get(rand.nextInt(nStation));
			if (i < 0.3*nBikes) {
				this.addBike(randomStation, BikeFactory.ELECTRIC, ADDING_DATE);
			} else {
				this.addBike(randomStation, BikeFactory.MECHANIC, ADDING_DATE);
			}
		}
		return net;
	}
	
	/**
	 * If the station is not full, this method creates a bike and adds it to the station
	 * @param s
	 * @param bikeType The bike type constant. Has to be taken from {@link BikeFactory}.
	 * @param changeTime The {@code LocalDateTime} object representing the moment when the bike is added to the station
	 */
	private void addBike(Station s, int bikeType, LocalDateTime changeTime) {
		if (!s.isStationFull()) {
			Slot slot = s.availableSlot();
			try {
				Bike b = BikeFactory.createBike(bikeType);
				slot.setBike(b, changeTime);
			} catch (NegativeTimeException e) {
				System.out.println("This shouldn't happen");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This functions returns an array of points in a square of side {@code s}. The area is divided in enough little squares to place all the points, and then each point is placed randomly on a little square. That ensures that each point is at a different place.
	 * @param N ({@code int}) the number of points
	 * @param s ({@code double}) the side of the square
	 * @return an array of {@code Point} where the points are equally distributed on the area
	 */
	public Point[] getPointDistribution(int N, double s) {
		int nSquares = 2;
		Point[] points = new Point[N];
		while (nSquares*nSquares < N) { nSquares++; }
		double side = s/nSquares;
		double x, y;
		int n = 0, i = 0, j = 0;
		while (i < nSquares && n < N) {
			j = 0;
			while (j < nSquares && n < N) {
				x = i*side + Math.random()*side;
				y = j*side + Math.random()*side;
				points[n] = new Point(x, y);
				j++; n++;
			}
			i++;
		}
		return points;
	}
	
	/**
	 * This method returns a {@code Network} corresponding to the name entered.
	 * @param name ({@code String}) the name of the network
	 * @return the {@code Network} corresponding to the name
	 * @throws InexistingNetworkNameException
	 */
	public Network findNetworkByName(String name) throws InexistingNetworkNameException {
		for (Network n : this.networks) {
			if (name.equals(n.getName())) { return n; }
		}
		throw new InexistingNetworkNameException(name);
	}
	
	/**
	 * This method returns a {@code Station} with the entered id.
	 * @param id the {@code id} of the {@code Station}
	 * @param net a {@code Network}
	 * @return
	 * @throws InexistingStationIdException
	 */
	public Station findStationByID(int id, Network net) throws InexistingStationIdException {
		for (Station s : net.getStations()) {
			if (s.getId() == id) { return s; }
		}
		throw new InexistingStationIdException(id, net);
	}

	/**
	 * This method adds a user to a network.
	 * @param userName
	 * @param cardType an {@code int} taken from {@link CardFactory}
	 * @param networkName
	 * @throws InexistingNetworkNameException
	 */
	public void addUser(String userName, int cardType, String networkName) throws InexistingNetworkNameException {
		User user = new User(userName);
		Network net = this.findNetworkByName(networkName);
		CardFactory cf = new CardFactory(net);
		cf.createCard(cardType, user);
	}
	
	/**
	 * This method sets a {@code Station} offline.
	 * @param networkName
	 * @param stationID
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 */
	public void setStationOffline(String networkName, int stationID) throws InexistingNetworkNameException, InexistingStationIdException {
		Network net = this.findNetworkByName(networkName);
		Station s = this.findStationByID(stationID, net);
		s.setOnline(false);
	}
	
	/**
	 * This method sets a {@code Station} online.
	 * @param networkName
	 * @param stationID
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 */
	public void setStationOnline(String networkName, int stationID) throws InexistingNetworkNameException, InexistingStationIdException {
		Network net = this.findNetworkByName(networkName);
		Station s = this.findStationByID(stationID, net);
		s.setOnline(true);
	}
	
	/**
	 * This method finds a {@code User} in a {@code Network} according to the user ID.
	 * @param id
	 * @param net
	 * @return a {@code User}
	 * @throws InexistingUserIdException
	 */
	public User findUserById(int id, Network net) throws InexistingUserIdException {
		User user = null;
		for (Card c : net.getCards()) {
			if (c.getUser().getId() == id) {
				user = c.getUser();
			}
		}
		if (user == null) {
			throw new InexistingUserIdException(id);
		} else {
			return user;
		}
	}
	
	/**
	 * This method finds a {@code Card} in a {@code Network} according to the owner ID.
	 * @param id
	 * @param net
	 * @return a {@code Card}
	 * @throws InexistingUserIdException
	 */
	public Card findCardByUserId(int id, Network net) throws InexistingUserIdException {
		Card card = null;
		for (Card c : net.getCards()) {
			if (c.getUser().getId() == id) {
				card = c;
			}
		}
		if (card == null) {
			throw new InexistingUserIdException(id);
		} else {
			return card;
		}
	}
	
	/**
	 * This methods makes a user rent a bike, without taking care of the type.
	 * @param userId
	 * @param stationId
	 * @param pickUpTime
	 * @param net
	 * @throws InexistingUserIdException
	 * @throws InexistingStationIdException
	 * @throws NegativeTimeException
	 * @throws OngoingRideException
	 * @throws NoBikeAvailableException
	 * @throws StationOfflineException
	 */
	public void rentBike(int userId, int stationId, LocalDateTime pickUpTime, Network net) throws InexistingUserIdException, InexistingStationIdException, NegativeTimeException, OngoingRideException, NoBikeAvailableException, StationOfflineException {
		Card card = this.findCardByUserId(userId, net);
		Station station = this.findStationByID(stationId, net);
		station.pickUpBike(card, pickUpTime);
	}
	
	/**
	 * This methods makes a user rent a bike of a specified type.
	 * @param userId
	 * @param stationId
	 * @param bikeType
	 * @param pickUpTime
	 * @param net
	 * @throws InexistingUserIdException
	 * @throws InexistingStationIdException
	 * @throws NegativeTimeException
	 * @throws NoElectricBikeAvailableException
	 * @throws OngoingRideException
	 * @throws StationOfflineException
	 * @throws NoMechanicBikeAvailableException
	 */
	public void rentBike(int userId, int stationId, int bikeType, LocalDateTime pickUpTime, Network net) throws InexistingUserIdException, InexistingStationIdException, NegativeTimeException, NoElectricBikeAvailableException, OngoingRideException, StationOfflineException, NoMechanicBikeAvailableException {
		Card card = this.findCardByUserId(userId, net);
		Station station = this.findStationByID(stationId, net);
		if (bikeType == BikeFactory.ELECTRIC) {
			station.pickUpElectricBike(card, pickUpTime);
		} else if (bikeType == BikeFactory.MECHANIC) {
			station.pickUpMechanicBike(card, pickUpTime);
		}
	}
	
	/**
	 * This method makes a user return a bike.
	 * @param userId
	 * @param stationId
	 * @param returnTime
	 * @param net
	 * @throws InexistingUserIdException
	 * @throws InexistingStationIdException
	 * @throws NegativeTimeException
	 * @throws NullDateException
	 * @throws NoSlotAvailableException
	 * @throws NoOngoingRideException
	 * @throws StationOfflineException
	 * @throws OngoingRideException 
	 */
	public double returnBike(int userId, int stationId, LocalDateTime returnTime, Network net) throws InexistingUserIdException, InexistingStationIdException, NegativeTimeException, NullDateException, NoSlotAvailableException, NoOngoingRideException, StationOfflineException, OngoingRideException {
		Card card = this.findCardByUserId(userId, net);
		Station station = this.findStationByID(stationId, net);
		double price = station.dropBike(card, returnTime);
		return price;
	}
	
	/**
	 * This method finds {@code Slot} according to its ID.
	 * @param id
	 * @param net
	 * @return a {@code Slot}
	 * @throws InexistingSlotIdException
	 */
	public Slot findSlotById(int id, Network net) throws InexistingSlotIdException {
		int stationId = id/1000;
		Station s = null;
		Slot sl = null;
		try {
			s = this.findStationByID(stationId, net);
		} catch (InexistingStationIdException e) {
			throw new InexistingSlotIdException(id);
		}
		if (s != null) {
			for (Slot slot : s.getParkingSlots()) {
				if (slot.getId() == id) {
					sl = slot;
				}
			}
		}
		if (sl != null) {
			return sl;
		} else {
			throw new InexistingSlotIdException(id);
		}
	}
	
	/**
	 * This method sets a slot online.
	 * @param networkName
	 * @param slotID
	 * @param changeTime
	 * @throws InexistingNetworkNameException
	 * @throws InexistingSlotIdException
	 * @throws NegativeTimeException
	 */
	public void setSlotOnline(String networkName, int slotID, LocalDateTime changeTime) throws InexistingNetworkNameException, InexistingSlotIdException, NegativeTimeException {
		Network net = this.findNetworkByName(networkName);
		Slot s = this.findSlotById(slotID, net);
		s.setOnline(true, changeTime);
	}
	
	/**
	 * This method sets a slot offline.
	 * @param networkName
	 * @param slotID
	 * @param changeTime
	 * @throws InexistingNetworkNameException
	 * @throws InexistingSlotIdException
	 * @throws NegativeTimeException
	 */
	public void setSlotOffline(String networkName, int slotID, LocalDateTime changeTime) throws InexistingNetworkNameException, InexistingSlotIdException, NegativeTimeException {
		Network net = this.findNetworkByName(networkName);
		Slot s = this.findSlotById(slotID, net);
		s.setOnline(false, changeTime);
	}

	/**
	 * This method adds one or more slots to a station.
	 * @param net
	 * @param stationID
	 * @param numSlots
	 * @throws InexistingStationIdException
	 */
	public void addSlot(Network net, int stationID, int numSlots) throws InexistingStationIdException {
		Station s = this.findStationByID(stationID, net);
		s.addSlot(numSlots);
	}
	
	/**
	 * This method adds a {@code StandardStation} to a {@code Network}.
	 * @param net
	 * @param numSlots
	 * @param x
	 * @param y
	 * @throws TypeStationException
	 * @throws StationSamePositionException
	 */
	public void addStandardStation(Network net, int numSlots, double x, double y) throws TypeStationException, StationSamePositionException {
		StationFactory sf = new StationFactory(net);
		Station s = sf.createStation("Standard", new Point(x, y));
		s.addSlot(numSlots);
	}
	
	/**
	 * This method adds a {@code PlusStation} to a {@code Network}.
	 * @param net
	 * @param numSlots
	 * @param x
	 * @param y
	 * @throws TypeStationException
	 * @throws StationSamePositionException
	 */
	public void addPlusStation(Network net, int numSlots, double x, double y) throws TypeStationException, StationSamePositionException {
		StationFactory sf = new StationFactory(net);
		Station s = sf.createStation("Plus", new Point(x, y));
		s.addSlot(numSlots);
	}
	
	/**
	 * This method adds an {@code ElectricBike} to the first free {@code Station} of a {@code Network}.
	 * @param net
	 * @param changeTime
	 * @throws NegativeTimeException
	 */
	public void addElectricBike(Network net, LocalDateTime changeTime) throws NegativeTimeException {
		Bike b = BikeFactory.createBike(BikeFactory.ELECTRIC);
		for (Station s : net.getStations()) {
			Slot sl = s.availableSlot();
			if (sl != null) {
				sl.setBike(b, changeTime);
			}
		}
	}
	
	public void addElectricBike(Network net, Station st, LocalDateTime changeTime) throws NoSlotAvailableException {
		if (st.isStationFull()) {
			throw new NoSlotAvailableException();
		} else {
			this.addBike(st, BikeFactory.ELECTRIC, changeTime);
		}
	}
	
	public void addMechanicBike(Network net, Station st, LocalDateTime changeTime) throws NoSlotAvailableException {
		if (st.isStationFull()) {
			throw new NoSlotAvailableException();
		} else {
			this.addBike(st, BikeFactory.MECHANIC, changeTime);
		}
	}
	
	/**
	 * This method adds a {@code MechanicBike} to the first free {@code Station} of a {@code Network}.
	 * @param net
	 * @param changeTime
	 * @throws NegativeTimeException
	 */
	public void addMechanicBike(Network net, LocalDateTime changeTime) throws NegativeTimeException {
		Bike b = BikeFactory.createBike(BikeFactory.MECHANIC);
		for (Station s : net.getStations()) {
			Slot sl = s.availableSlot();
			if (sl != null) {
				sl.setBike(b, changeTime);
			}
		}
	}
	
	/**
	 * This method returns an {@code ArrayList} of stations ordered according to a specific {@code SortingStrategy}.
	 * @param net
	 * @param s
	 * @return a sorted {@code ArrayList} of stations.
	 */
	public ArrayList<Station> sortStations(Network net, SortingStrategy s) {
		return net.sortingStations(s);
	}
	
	/**
	 * This function resets the networks.
	 */
	public void resetNetworks() {
		this.networks = new ArrayList<Network>();
	}

}
