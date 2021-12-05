package card;

import bike.MechanicBike;
import bike.ElectricBike;

/**
 * This interface represents the cards used to pay the rides.
 * @author Pietro Dellino
 * @see VlibreCard
 * @see VmaxCard
 * @see CreditCard
 */
public interface CardVisitor {
	
	/**
	 * This method calculates the price of a ride for a user using an {@code ElectricBike}.
	 * @param bike The bike used for the ride.
	 * @param rideTime The duration of the ride, in minutes.
	 */
	public float computeRidePrice(ElectricBike bike, int rideTime);
	
	/**
	 * This method calculates the price of a ride for a user using a {@code MechanicBike}.
	 * @param bike The bike used for the ride.
	 * @param rideTime The duration of the ride, in minutes.
	 */
	public float computeRidePrice(MechanicBike bike, int rideTime);

}
