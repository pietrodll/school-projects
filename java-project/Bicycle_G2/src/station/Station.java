package station;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tools.*;
import user.Observable;
import user.Observer;
import user.User;
import bike.*;
import card.Card;
import ride.Network;

/**
 * An abstract class to represent the stations. Each {@code Station} has a unique {@code id} (regardless of its type). A {@code Station} is part of a {@code Network}, has a position, a number of {@code Slot} objects. A {@code Station} is online when created. 
 * @author Chloé
 * @see StandardStation
 * @see PlusStation
 */
public abstract class Station implements Observable {
	
	private int id;
	private Point p;
	private boolean isOnline;
	private ArrayList<Slot> parkingSlots;
	private Network net;
	
	private int totalRents;
	private int totalReturns;
	private int totalOperations;

	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private boolean changed; 

	public Station(Point p, Network net) {
		this.p = p;
		id = StationIdGenerator.getInstance().getNextStationID();
		this.parkingSlots = new ArrayList<Slot>();
		this.isOnline = true;
		this.changed = false;
		this.net = net;
		
		
	}

	/**
	 * This method calculates the rate of occupation of an entire {@code Station} during a time period.
	 * @param startTime
	 * @param endTime
	 * @return Rate of occupation during startTime and endTime
	 * @throws NoSlotStateAtDateException
	 * @throws NegativeTimeException
	 * @throws NullDateException
	 */
	public double getRateOccupation(LocalDateTime startTime, LocalDateTime endTime) throws NoSlotStateAtDateException, NegativeTimeException, NullDateException{
		double occupationRate = -1;
		ArrayList<Slot> existingSlots = new ArrayList<Slot>();
		for (Slot slot : parkingSlots) {
			if (slot.computeOccupationTime(startTime, endTime) != -1) {
				existingSlots.add(slot);
			}
		}
		if (existingSlots.size() == 0) {
			throw new NoSlotStateAtDateException(endTime);
		}
		try {
			int totalOccupationTime = 0;
			int delta = Date.computeTime (startTime, endTime);
			int N = existingSlots.size();
			for (Slot slot : existingSlots) {
				int time = slot.computeOccupationTime(startTime, endTime);
				totalOccupationTime =(int) + time;
			}
			occupationRate = (double) (totalOccupationTime / (delta * N));
		} catch (NegativeTimeException e) {
			System.out.println("Error : Could not calculate rate Occupation");
		}
		return occupationRate;
	}
	
	/**
	 * A {@code Station} is full when it is offline, or when all of its {@code Slots} are occupied.
	 * @return if the {@code Station} is full
	 */
	public boolean isStationFull() {
		if (this.isOnline==false) {return true;}
		for (Slot s : parkingSlots) {
			if (!s.getisOccupied()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return an available {@code Slot}
	 */
	public Slot availableSlot() {
		if (this.isOnline) {
			for (Slot s : parkingSlots) {
				if (s.getisOccupied() == false) {
					return s; }
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return a {@code Slot} online and with an available {@code Bike}
	 */
	public Slot hasBikeAvailable() {
		if (this.isOnline) {
			for (Slot s : parkingSlots) {
				if (s.isOnline() == true && s.getBike() != null) {
					return s;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return a {@code Slot} online and with an available {@code ElectricBike}
	 */
	public Slot hasElectricBikeAvailable() {
		if (this.isOnline) {
			for (Slot s : parkingSlots) {
				if (s.isOnline() && s.getBike() instanceof ElectricBike) {
					return s;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return a {@code Slot} online and with an available {@code MechanicBike}
	 */
	public Slot hasMechanicBikeAvailable() {
		if (this.isOnline) {
			for (Slot s : parkingSlots) {
				if (s.isOnline() && s.getBike() instanceof MechanicBike) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param card
	 * @return {@code User} of the card
	 */
	public User identifyUser (Card card) {
		if (this.isOnline) {
			return card.getUser();
		}
		else {return null;} 
	}
	
	
	/**
	 * This method allows to pick up a {@code Bike} if the {@code Station} is online, if the {@code User} of the {@code Card} has no ongoing {@code Ride} and if there is an a {@code Slot} with a {@code Bike} available
	 * @param card
	 * @param pickUpTime
	 * @throws NegativeTimeException
	 * @throws OngoingRideException 
	 * @throws NoBikeAvailableException 
	 * @throws StationOfflineException 
	 */
	public synchronized void pickUpBike(Card card, LocalDateTime pickUpTime) throws NegativeTimeException, OngoingRideException, NoBikeAvailableException, StationOfflineException {
		if (this.isOnline) {
			User user = identifyUser (card);
			if (user.getOngoingRide() == null) {
				Slot s = hasBikeAvailable();
				if (s != null) {
					Bike b = s.getBike();
					user.startOngoingRide(this.net, b, pickUpTime, card);
					s.setBike(null, pickUpTime);
					this.setTotalRents(getTotalRents()+1);
					user.setPosition(p); 
				} else { throw new NoBikeAvailableException() ;
				}
			} else {throw new OngoingRideException();	
			}
		} else  {throw new StationOfflineException(this);}
	}
	
	/**
	 * This method allows to pick up a {@code ElectricBike} if the {@code Station} is online, if the {@code User} of the {@code Card} has no ongoing {@code Ride} and if there is an a {@code Slot} with a {@code ElectricBike} available
	 * @param card
	 * @param pickUpTime
	 * @throws NegativeTimeException
	 * @throws NoElectricBikeAvailableException 
	 * @throws OngoingRideException 
	 * @throws StationOfflineException 
	 */
	public synchronized void pickUpElectricBike(Card card, LocalDateTime pickUpTime) throws NegativeTimeException, NoElectricBikeAvailableException, OngoingRideException, StationOfflineException {
		if (this.isOnline) {
			User user = identifyUser (card);
			if (user.getOngoingRide() == null) {
				Slot s = hasElectricBikeAvailable();
				if (s != null) {
					Bike b = s.getBike();
					user.startOngoingRide(this.net, b, pickUpTime, card);
					s.setBike(null, pickUpTime);
					this.setTotalRents(getTotalRents()+1);
					user.setPosition(p);
				}else {throw new NoElectricBikeAvailableException() ;
				}
			} else {throw new OngoingRideException();	
			}
		} else  {throw new StationOfflineException(this);
		} 
	}
	
	/**
	 * This method allows to pick up a {@code MechanicBike} if the {@code Station} is online, if the {@code User} of the {@code Card} has no ongoing {@code Ride} and if there is an a {@code Slot} with a {@code MechanicBike} available
	 * @param card
	 * @param pickUpTime
	 * @throws NegativeTimeException
	 * @throws NoMechanicBikeAvailableException 
	 * @throws OngoingRideException 
	 * @throws StationOfflineException 
	 */
	public synchronized void pickUpMechanicBike(Card card, LocalDateTime pickUpTime) throws NegativeTimeException, NoMechanicBikeAvailableException, OngoingRideException, StationOfflineException {
		if (this.isOnline) {
			User user = identifyUser (card);
			if (user.getOngoingRide() == null) {
				Slot s = hasMechanicBikeAvailable();
				if (s != null) {
					Bike b = s.getBike();
					user.startOngoingRide(this.net, b, pickUpTime, card);
					s.setBike(null, pickUpTime);
					this.setTotalRents(getTotalRents()+1);
					user.setPosition(p);
				}else {throw new NoMechanicBikeAvailableException() ;
				}
			} else {throw new OngoingRideException();	
			}
		} else  {throw new StationOfflineException(this);
		}
	}
	
	/**
	 * This method allows to drop a {@code Bike} if the {@code Station} is online, if the {@code User} of the {@code Card} has an ongoing {@code Ride} and if there is an available {@code Slot}.
	 * If the {@code Station} becomes full, the changed attributes become true so that the observers are notified.
	 * @param card
	 * @param dropTime
	 * @throws NegativeTimeException
	 * @throws NullDateException 
	 * @throws NoSlotAvailableException 
	 * @throws NoOngoingRideException 
	 * @throws StationOfflineException 
	 * @return price of the {@code Ride}
	 * @throws OngoingRideException 
	 */
	public synchronized double dropBike (Card card, LocalDateTime dropTime) throws NegativeTimeException, NullDateException, NoSlotAvailableException, NoOngoingRideException, StationOfflineException, OngoingRideException {
		if (this.isOnline) {	
			User user = identifyUser(card);
			if (user.getOngoingRide() != null) {
				if (this.isStationFull() == false) {
					Slot s = availableSlot();
					s.setBike(user.getOngoingRide().getBike(), dropTime);
					this.setTotalReturns(getTotalReturns()+1);
					double price = user.endOngoingRide(dropTime);
					user.setPosition(p);
					if (this.isStationFull() == true) {
							this.changed = true;
							this.notifyObservers();
					}
					return price;
				} else {
					throw new NoSlotAvailableException();
				}
			} else {
				throw new NoOngoingRideException();	
			}
		}else  {
			throw new StationOfflineException(this);
		}
	}
	
	@Override
	public void registerObserver (Observer observer) {
		observers.add(observer);}


	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);}


	@Override 
	public void notifyObservers () {
		if (this.changed) {
			ArrayList<User> UserToRemoveFromObserversNoItinerary = new ArrayList<User>();
			ArrayList<User> UserToRemoveFromObserversItinerary = new ArrayList<User>();
			for (Observer ob : observers) {
				int i = ob.update();
				if (i == 0) {
					UserToRemoveFromObserversNoItinerary.add((User) ob);
				}
				if (i == 1) {
					UserToRemoveFromObserversItinerary.add((User) ob);
				}
			}
			for (User user : UserToRemoveFromObserversNoItinerary) {
				user.setItinerary(null);
			}
			for (User user : UserToRemoveFromObserversItinerary) {
				if (user.getItinerary().getEndStation() != this) {
					this.getObservers().remove(user);
				}
			}	
		} this.changed = false;
		
	}
	
	public Point getP() { return p; }

	public void setP(Point p) { this.p = p; }

	public boolean isOnline() { return isOnline; }

	/**
	 * This method sets the boolean isOnline. If a Station goes offline, all its Observers are notified.
	 * @param isOnline
	 */
	public void setOnline(boolean isOnline) { 
		boolean wasOnline = this.isOnline;
		this.isOnline = isOnline; 
		if (wasOnline == true && isOnline == false) {
			this.changed = true;
			this.notifyObservers();
		}
	}

	public ArrayList<Slot> getParkingSlots() { return parkingSlots; }

	public void addSlot() {
		this.parkingSlots.add(new Slot(this)); }
	
	public void addSlot(int n) {
		for (int i = 0; i<n; i++) {
			this.parkingSlots.add(new Slot(this)); }
	}
	
	public void removeSlot (Slot slot) {
		for (Slot s : parkingSlots) {
			if (s.equals(slot)) {
				parkingSlots.remove(s);
			}
		}
	}

	public int getTotalRents() { return totalRents; }

	public void setTotalRents(int totalRents) {
		this.totalRents = totalRents;
		this.totalOperations = this.totalRents + this.totalReturns;
	}

	public int getTotalReturns() { return totalReturns; }

	public void setTotalReturns(int totalReturns) {
		this.totalReturns = totalReturns;
		this.totalOperations = this.totalRents + this.totalReturns;
	}
	
	public int getTotalOperations() { return totalOperations; }
	
	public int getId() { return id; }
	
	public boolean isChanged() { return changed; }

	public void setChanged(boolean changed) { this.changed = changed; }

	public ArrayList<Observer> getObservers() {	return observers; }

	/**
	 * Redefinition of the equals() method
	 */
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public String toString() {
		return "Station" + this.getId();
	}
}
