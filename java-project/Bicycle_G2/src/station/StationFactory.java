package station;

import ride.Network;
import tools.Point;

/**
 * This class creates {@code Station} objects.
 * @author Chloé
 * @see Station
 */
public class StationFactory  {
	
	public Network net;
	
	public StationFactory(Network net) { this.net = net; }
	
	/**
	 * When creating a new {@code Station}, the method checks if there is no other stations at the same position.
	 * @param stationType
	 * @param p
	 * @return the Station created
	 * @throws TypeStationException
	 * @throws StationSamePositionException
	 */
	public Station createStation (String stationType, Point p ) throws TypeStationException, StationSamePositionException {
		for (Station s : this.net.getStations()) {
			if (s.getP().equals(p) ) { 
				throw new StationSamePositionException(p); }
		}
		
		if (stationType.equalsIgnoreCase("Standard")) {
			Station s1 = new StandardStation(p, net);
			this.net.addStation(s1);
			return s1;
		} else if (stationType.equalsIgnoreCase("Plus")) {
			Station s2 = new PlusStation(p, net);
			this.net.addStation(s2);	
			return s2;
		} else {
			throw new TypeStationException(stationType);
		}
	}
		
}
