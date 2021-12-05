package station;

import ride.Network;
import tools.Point;
/**
 * This class represents Standard stations, it extends the class {@code Station}.
 * @author Chloé
 * @see Station
 */
public class StandardStation extends Station  {
	
	public StandardStation (Point p, Network net) {
		super (p, net);
	}

	/**
	 * Redefinition of the equals() method
	 */
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof StandardStation) {
			StandardStation other = (StandardStation) obj;
			res = this.getId() == other.getId();
		}
		return res;
	}
	
	

}
