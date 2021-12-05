package bike;

/**
 * This class creates {@code Bike} objects.
 * @author Pietro Dellino
 * @see Bike
 */
public class BikeFactory {
	
	public static final int ELECTRIC = 1;
	public static final int MECHANIC = 2;
	
	public static Bike createBike(int bikeType) {
		switch (bikeType) {
		case ELECTRIC:
			return new ElectricBike();
		case MECHANIC:
			return new MechanicBike();
		default:
			return null;
		}
	}

}
