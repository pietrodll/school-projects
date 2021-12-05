package ride.path;

import station.Station;
import tools.Point;

/**
 * This class is used to compare stations according to their distance to a point.
 * @author Pietro Dellino
 */
public class DistanceBasicComparator extends StationComparator {

	public DistanceBasicComparator(Point p) {
		super(p);
	}

	@Override
	public int compare(Station arg0, Station arg1) {
		double distanceDiff = this.getDistanceDiff(arg0, arg1);
		return distanceDiff < 0 ? -1 : distanceDiff > 0 ? 1 : 0;
	}

}
