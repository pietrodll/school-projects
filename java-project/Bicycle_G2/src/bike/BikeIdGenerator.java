
package bike;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class BikeIdGenerator implements Serializable {
	
	/**
	 * This serial version UID has been generated by Papyrus.
	 */
	private static final long serialVersionUID = -989823147956520121L;
	private static BikeIdGenerator instance = null;
	private int numBikes;
	
	private BikeIdGenerator() {}
	
	/**
	 * This method returns the unique instance of the class (singleton pattern).
	 */
	public static synchronized BikeIdGenerator getInstance() {
		if (instance == null) { instance = new BikeIdGenerator(); }
		return instance;
	}
	
	public synchronized int getNextId() { return this.numBikes++; }
	
	/**
	 * This methods ensures that even when an {@code CardIdGenerator} is serialized, the unique instance is always returned.
	 * @return The unique instance of the class.
	 * @throws ObjectStreamException.
	 */
	public Object readResolve() throws ObjectStreamException { return instance; }

}