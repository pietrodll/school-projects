package bike;

import card.CardVisitor;

public interface CardVisitable {
	
	public float ridePrice(CardVisitor card, int rideTime);

}
