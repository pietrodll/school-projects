package ride;
import java.time.LocalDateTime;

import bike.*;
import card.Card;
import station.OngoingRideException;
import user.*;
import tools.Date;
import tools.NegativeTimeException;
import tools.NullDateException;

/**
 * This class represents a {@code Ride} of a {@code Network}. It has a starting and ending time, a {@code User} with its {@code Card} and a {@code Bike}. It is created without an ending time.
 * @author Chloé
 *
 */
public class Ride {
	
	private Bike bike;
	private User user;
	private Card card;
	private LocalDateTime startRide;
	private LocalDateTime endRide; 
	private int rideTime;
	private Network net;
	
	
	public Ride(Network net, Bike bike, User user, Card card, LocalDateTime startRide) {
		super();
		this.net = net;
		this.bike = bike;
		this.user = user;
		this.card = card;
		this.startRide = startRide;
	}
	

	public Network getNet() { return this.net; }
	public Bike getBike() { return bike; }
	public User getUser() { return user; }
	public LocalDateTime getStartRide() { return startRide; }
	public LocalDateTime getEndRide() { return endRide; }
	public Card getCard() { return card; }
	
	/**
	 * This method sets the ending time of the {@code Ride}, and archive it in the Ride History of the {@code Network}.	 
	 * @param endRide
	 * @throws NullDateException 
	 * @throws NegativeTimeException 
	 */
	public void endRide(LocalDateTime endRide) throws NegativeTimeException, NullDateException {
		this.endRide = endRide;
		net.archiveRide(this);
		this.rideTime = Date.computeTime(this.startRide, this.endRide);
		
	}
	
	/**
	 * 
	 * @return the duration of the {@code Ride}
	 * @throws NegativeTimeException
	 * @throws NullDateException
	 * @throws OngoingRideException 
	 */
	public int getRideTime() throws NegativeTimeException, NullDateException, OngoingRideException {
		if (this.endRide != null) {
			return this.rideTime;
		} else { throw new OngoingRideException();
		}
	}
	

	

}
