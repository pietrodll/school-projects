package bike;

import card.CardVisitor;

/**
 * A class representing bikes. Each {@code Bike} has an unique id, regardless of the type.
 * @author Pietro Dellino
 */
public abstract class Bike implements CardVisitable {
	
	private int id;
	
	public Bike() {
		BikeIdGenerator idGenerator = BikeIdGenerator.getInstance();
		this.id = idGenerator.getNextId();
	}

	public int getId() { return id; }
	
	public abstract float ridePrice(CardVisitor card, int rideTime);
	
	@Override
	public abstract boolean equals(Object obj);

}
