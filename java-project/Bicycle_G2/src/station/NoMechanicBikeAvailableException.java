package station;

/**
 * This exception is thrown when a {@code User} tries to pick up a {@code MechanicBike} from a {@code Station} that has no {@code MechanicBike} available.
 * @author Chlo�
 *
 */
public class NoMechanicBikeAvailableException extends Exception {

	/**
	 * Generated by Papyrus
	 */
	private static final long serialVersionUID = -5629390867860698689L;
	

	@Override
	public String getMessage() {
		return "There is no mechanic bike available in this station.";
	}
}
