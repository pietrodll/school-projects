
package user;

import ride.Itinerary;
import ride.Network;
import ride.Ride;
import ride.path.PathStrategy;
import station.NoOngoingRideException;
import station.OngoingRideException;
import station.Station;
import tools.NegativeTimeException;
import tools.NullDateException;
import tools.Point;
import ui.clui.CommandLineDisplay;
import ui.clui.CommandLineReader;

import java.time.LocalDateTime;
import bike.Bike;
import card.Card;

/**
 * This class represents the users of the network. A {@code User} is created with a Username, and can have or not an initial position.
 * Each {@code User} has a unique {@code id} and its own {@code UserStat} class that contains his statistics.
 * A {@code User} can have an Ongoing {@code Ride} and can be following an {@code Itinerary}.
 * @author Chloé
 * @see UserStat
 * @see UserIdGenerator
 */
public class User implements Observer {
	
	private String userName;
	private Point position;
	private final int id;
	private Ride ongoingRide;
	private Itinerary itinerary;
	private UserStat userStat;
	
	
	public User(String userName) {
		super();
		this.userName = userName;
		id = UserIdGenerator.getInstance().getNextUserID();
		position = new Point (0,0);
		ongoingRide = null;
		itinerary = null;
		userStat = new UserStat();
	}
	
	public User(String userName, Point position) {
		super();
		this.userName = userName;
		id = UserIdGenerator.getInstance().getNextUserID();
		this.position = position;
		ongoingRide = null;
		itinerary = null;
		userStat = new UserStat();
	}
	
	/**
	 * If the {@code User} does not have an ongoing {@code Ride}, this method sets a new ongoing {@code Ride}. 
	 * @param net
	 * @param bike
	 * @param startRide
	 * @param card
	 * @throws OngoingRideException  
	 */
	public void startOngoingRide (Network net, Bike bike, LocalDateTime startRide, Card card) throws OngoingRideException {
		if (ongoingRide == null) {
			this.ongoingRide = new Ride (net, bike, this, card, startRide);
		} else {throw new OngoingRideException();}
	}
	
	/**
	 * If the {@code User} has an ongoing {@code Ride}, this method ends the ongoing {@code Ride}, resets the {@code Itinerary}, computes the cost of the ride and updates the user's statistics.
	 * The method removes the {@code User} from the Observers of the arrival {@code Station}.
	 * @param endRide
	 * @throws NegativeTimeException
	 * @throws NullDateException 
	 * @throws NoOngoingRideException 
	 * @throws OngoingRideException
	 */
	public double endOngoingRide(LocalDateTime endRide) throws NegativeTimeException, NullDateException, NoOngoingRideException, OngoingRideException {
		if (this.ongoingRide != null) {
			if (this.itinerary != null) {
				this.itinerary.getEndStation().removeObserver(this);
				this.itinerary = null;	
			}
			ongoingRide.endRide(endRide);
			int timeRide = ongoingRide.getRideTime();
			double price = ongoingRide.getBike().ridePrice(ongoingRide.getCard(), timeRide);
			this.userStat.addRide();
			this.userStat.addAmount((double)price);	
			this.userStat.addTime(timeRide);
			this.ongoingRide = null;
			return price;
		} else {throw new NoOngoingRideException();}
	}
	
	
	/**
	 * This method calculates an itinerary given a starting position, an arrival position and a {@code PathStrategy}. 
	 * @param start
	 * @param arrival
	 * @param ps
	 * @return itinerary
	 */
	public Itinerary calculateItinerary(Point start, Point arrival, PathStrategy ps) {
		Itinerary i1 = new Itinerary(start, arrival);
		i1.computePath(ps);
		return i1;
	}
	
	/**
	 * This method calculates an itinerary given a starting position, an arrival position and a {@code PathStrategy}, and considering the {@code BikeType} wanted.
	 * @param start
	 * @param arrival
	 * @param ps
	 * @param bikeType
	 * @return itinerary
	 */
	public Itinerary calculateItinerary(Point start, Point arrival, PathStrategy ps, int bikeType) {
		Itinerary i1 = new Itinerary(start, arrival);
		i1.computePath(ps, bikeType);
		return i1;		
	}
	
	
	/**
	 * This method sets the {@code Itinerary} of a {@code User}. The {@code User} is added to the observer list of the arrival {@code Station}.
	 * @param itinerary
	 */
	public void setItinerary(Itinerary itinerary) { 
		if (this.itinerary == null) {
			if (itinerary != null) {
				itinerary.getEndStation().registerObserver(this);
				this.itinerary = itinerary; 
			}				
		} else {
			if (itinerary == null) {
				Station s = this.itinerary.getEndStation();
				s.removeObserver(this);
				this.itinerary = itinerary;
			} 
			else {
				this.itinerary.getEndStation().removeObserver(this);
				itinerary.getEndStation().registerObserver(this);
				this.itinerary = itinerary; 
			}		
		}
	}
	
	/**
	 * The {@code User} is notified if he has an {@code Itinerary}, an ongoing {@code Ride} and if the arrival {@code Station} of its {@code Itinerary} becomes full. He has the possibility to recalculte the ending {@code Station}.
	 */
	@Override
	public int update() {
		CommandLineReader clr = new CommandLineReader();
		CommandLineDisplay cld = new CommandLineDisplay();
		cld.display("Notification : The destination Station does not have any more available slots or is offline.");
		if (this.ongoingRide != null) {
			String s = clr.readCommand("Do you want to recalculate arrival station ? Answer 'yes' if you do.");
			if (s.equals("yes")) {
				if (this.itinerary != null) {
					PathStrategy ps = this.itinerary.getPs();
					Station s1 = ps.findEndStation(this.itinerary.getStartStation().getP(), this.itinerary.getEnd(), this.getOngoingRide().getBike());
					this.itinerary.setEndStation(s1);
					s1.registerObserver(this);
				}
				return 1;
			} 
			else {
				return 0;}
		} else {
			return 0;
			
			}
	}
	
	public String getUserName() { return userName; }

	public Point getPosition() { return position; }
	public void setPosition(Point position) { this.position = position; }

	public Ride getOngoingRide() { return ongoingRide; }

	public Itinerary getItinerary() { return itinerary; }

	public int getId() { return id; }

	public UserStat getUserStat() { return userStat; }
	
	/**
	 * This method redefines the equals method for the {@code User} class. {@code User} are compared on their ID.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof User) {
			User other = (User) obj;
			res = this.getId() == other.getId();
		}
		return res;
	}
}