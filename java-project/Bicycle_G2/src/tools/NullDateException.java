package tools;
/**
 * This exception is thrown when a date is null and methods are using the date
 * @author Chlo?
 *
 */
public class NullDateException extends Exception {

	/**
	 * Generated by Papyrus
	 */
	private static final long serialVersionUID = -7067785442305650426L;
	
	
	@Override
	public String getMessage() {
		return "Error : The date is null";
	}
	
}
