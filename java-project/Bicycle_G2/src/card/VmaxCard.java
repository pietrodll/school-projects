package card;

import bike.Bike;
import bike.ElectricBike;
import bike.MechanicBike;
import user.User;

/**
 * This class represents VMax Cards.
 * Its methods allow to calculate the price of a ride when the users has this type of card.
 * @author Pietro Dellino
 * @see CardVisitor
 */
public class VmaxCard extends Card {

	public VmaxCard(User user) {
		super(user);
	}

	/**
	 * This method calculates the price of a ride for a user having a {@code VmaxCard} when he uses an {@code ElectricBike}.
	 * @param bike The bike used for the ride.
	 * @param rideTime The duration of the ride, in minutes.
	 */
	@Override
	public float computeRidePrice(ElectricBike bike, int rideTime) {
		return computeVmax(bike, rideTime);
	}

	/**
	 * This method calculates the price of a ride for a user having a {@code VmaxCard} when he uses a {@code MechanicBike}.
	 * @param bike The bike used for the ride.
	 * @param rideTime The duration of the ride, in minutes.
	 */
	@Override
	public float computeRidePrice(MechanicBike bike, int rideTime) {
		return computeVmax(bike, rideTime);
	}
	
	private float computeVmax(Bike bike, int rideTime) {
		float cost = 0;
		if (rideTime > 60) {
			int exceedingTime = rideTime - 60;
			int credit = this.getTimeCredit();
			if (credit >= exceedingTime) {
				try {
					this.useCredit(exceedingTime);
				} catch (InsufficientCreditException e) {
					e.printStackTrace();
					this.setTimeCredit(0);
				}
			} else {
				try {
					this.useCredit(credit);
				} catch (InsufficientCreditException e) {
					e.printStackTrace();
					this.setTimeCredit(0);
				} finally {
					cost = (exceedingTime - credit)/60;
				}
			}
		}
		return cost;
	}

	@Override
	public String getTypeString() {
		return "Vmax";
	}

}
