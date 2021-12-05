package ride.path;

import java.util.Comparator;

import bike.BikeFactory;
import station.Station;
import tools.Point;

/**
 * This class contains some methods that are useful for the comparators.
 * @see DistanceStartComparator
 * @see DistanceEndComparator
 * @see DistanceBasicComparator
 * @see FastestPathComparator
 * @author Pietro Dellino
 *
 */
public abstract class StationComparator implements Comparator<Station> {
	
	protected Point point;
	protected int bikeType = 0;
	
	public StationComparator(Point p) {
		this.point = p;
	}
	
	public StationComparator(Point p, int bikeType) {
		this.point = p;
		if (bikeType == BikeFactory.ELECTRIC || bikeType == BikeFactory.MECHANIC) {
			this.bikeType = bikeType;
		}
	}
	
	public Point getPoint() { return point; }
	public void setPoint(Point point) { this.point = point; }

	@Override
	public abstract int compare(Station arg0, Station arg1);
	
	/**
	 * 
	 * @param s1 The first {@code Station}
	 * @param s2 The second {@code Station}
	 * @return The difference between the distances of the stations to {@code this.point}
	 */
	public double getDistanceDiff(Station s1, Station s2) {
		return this.point.distancePoint(s1.getP()) - this.point.distancePoint(s2.getP());
	}
	
	public static int availabilityComparator(double distanceDiff, boolean available1, boolean available2) {
		int res = 0;
		if (distanceDiff < 0) {
			res = available1 ? -1 : available2 ? 1 : 0;
		} else if (distanceDiff > 0) {
			res = available2 ? 1 : available1 ? -1 : 0;
		} else {
			res = !available1 ? (!available2 ? 0 : 1) : (available2 ? 0 : -1);
		}
		return res;
	}

}
