package user;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * An ID generator for the users.
 * @author Chlo�
 *
 */
public class UserIdGenerator implements Serializable {
	
	/**
	 * Generated by Papyrus.
	 */
	private static final long serialVersionUID = -2557421117736232615L;
	
	private static UserIdGenerator instance = null;
	private int num;
	
	private UserIdGenerator() {}
	
	/**
	 * This method returns the unique instance of the class (singleton pattern).
	 */
	public static synchronized UserIdGenerator getInstance() {
		if (instance == null) {
			instance = new UserIdGenerator();
		}
		return instance;
	}
	
	public synchronized int getNextUserID() { return num ++; }
	
	/**
	 * This methods ensures that even when an {@code UserIDGenerator} is serialized, the unique instance is always returned.
	 * @return The unique instance of the class.
	 * @throws ObjectStreamException
	 */
	public Object readResolve() throws ObjectStreamException {
		return instance;
	}

}