package controller;

/**
 * This exception is thrown when creating a {@code Network} with a name that already exists.
 * @author Chlo?
 *
 */
public class ExistingNameException extends Exception {
	
	/**
	 * Generated by Papyrus
	 */
	private static final long serialVersionUID = -8234682889201570286L;
	private String name;
	
	public ExistingNameException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		String message = "The name \"" + this.name + "\" cannot be used, a network with this name already exists.";
		return message;
	}

}
