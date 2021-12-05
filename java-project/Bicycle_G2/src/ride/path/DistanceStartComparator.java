package ride.path;

import bike.BikeFactory;
import station.Station;
import tools.Point;

/**
 * This class is used to compare stations according to their distance to a point and to the bikes that are available in them. It is used to find the best station to start a ride.
 * @author Pietro Dellino
 *
 */
public class DistanceStartComparator extends StationComparator {
	
	public DistanceStartComparator(Point p, int bikeType) {
		super(p, bikeType);
	}
	
	public DistanceStartComparator(Point p) {
		super(p);
	}

	/**
	 * Compares the stations according to their distance to {@code this.point}.
	 * According to the value of {@code bikeType} defined when the comparator is instantiated, it takes into account the availability of the bikes.
	 */
	@Override
	public int compare(Station arg0, Station arg1) {
		int res = 0;
		double distanceDiff = this.getDistanceDiff(arg0, arg1);
		if (bikeType == 0) {
			res = StationComparator.availabilityComparator(distanceDiff, arg0.hasBikeAvailable() != null, arg1.hasBikeAvailable() != null);
		} else if (bikeType == BikeFactory.ELECTRIC) {
			res = StationComparator.availabilityComparator(distanceDiff, arg0.hasElectricBikeAvailable() != null, arg1.hasElectricBikeAvailable() != null);
		} else {
			res = StationComparator.availabilityComparator(distanceDiff, arg0.hasMechanicBikeAvailable() != null, arg1.hasMechanicBikeAvailable() != null);
		}
		return res;
	}

}
