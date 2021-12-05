package ride.path;

import station.Station;
import tools.Point;

/**
 * This class is used to compare stations according to their distance to a point and to their free slots. It is used to find the best station to finish a ride.
 * @author Pietro Dellino
 */
public class DistanceEndComparator extends StationComparator {
	
	public DistanceEndComparator(Point point) { super(point); }

	@Override
	public int compare(Station arg0, Station arg1) {
		int res = 0;
		double distanceDiff = this.getDistanceDiff(arg0, arg1);
		res = StationComparator.availabilityComparator(distanceDiff, !arg0.isStationFull(), !arg1.isStationFull());
		return res;
	}

}
