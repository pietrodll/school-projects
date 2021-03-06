package station;

/**
 * This exception is thrown when the client enters a non valid type of {@code Station} in the {@code StationFactory}.
 * @author Chlo?
 *
 */
public class TypeStationException extends Exception {
	
	/**
	 * Generated by Papyrus
	 */
	private static final long serialVersionUID = 4145615811670724618L;
	private String type;

	public TypeStationException(String type) {
		super();
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return "Error : the type of Station' " + this.type + " ' does not exist ";
}
	
	

}
