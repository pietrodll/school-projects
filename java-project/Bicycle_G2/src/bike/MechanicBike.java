package bike;

import card.CardVisitor;

public class MechanicBike extends Bike {
	
	/**
	 * This methods computes the price of a ride according to the user card.
	 * @see Bike#ridePrice(card.Card, int)
	 */
	@Override
	public float ridePrice(CardVisitor card, int rideTime) {
		return card.computeRidePrice(this, rideTime);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof MechanicBike) {
			MechanicBike other = (MechanicBike) obj;
			res = this.getId() == other.getId();
		}
		return res;
	}

}
