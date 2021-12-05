package ride.path;

import java.util.ArrayList;
import java.util.Collections;

import bike.Bike;
import ride.Network;
import station.Station;
import tools.Point;

/**
 * This class is an implementation of the {@code PathStrategy} interface and finds the pickup station and return station which minimize the walking distance of the user.
 * @author Pietro Dellino
 * @see PathStrategy
 */
public class MinimalWalkingStrategy implements PathStrategy {
	
	private ArrayList<Station> stations;
	
	public MinimalWalkingStrategy(Network net) {
		this.stations = net.getStations();
	}

	@Override
	public Station[] findPath(Point start, Point end, int bikeType) {
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start, bikeType);
		stations[0] = Collections.min(this.stations, distanceComp);
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		stations[1] = Collections.min(this.stations, distEndComp);
		return stations;
	}

	@Override
	public Station[] findPath(Point start, Point end) {
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start);
		stations[0] = Collections.min(this.stations, distanceComp);
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		stations[1] = Collections.min(this.stations, distEndComp);
		return stations;
	}

	@Override
	public Station findEndStation(Point start, Point end, Bike bike) {
		DistanceEndComparator dec = new DistanceEndComparator(end);
		Station station = Collections.min(this.stations, dec);
		return station;
	}

}
