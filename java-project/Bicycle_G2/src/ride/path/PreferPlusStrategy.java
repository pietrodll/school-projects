package ride.path;

import java.util.ArrayList;
import java.util.Collections;

import bike.Bike;
import ride.Network;
import station.PlusStation;
import station.Station;
import tools.Point;

/**
 * This {@code PathStrategy} prefers a {@code PlusStation} if it is less than 10% further than the closest station.
 * @author Pietro Dellino
 * @see PathStrategy
 */
public class PreferPlusStrategy implements PathStrategy {
	
	private ArrayList<Station> stations;
	
	public PreferPlusStrategy(Network net) {
		this.stations = net.getStations();
	}

	
	@Override
	public Station[] findPath(Point start, Point end, int bikeType) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start, bikeType);
		stations[0] = Collections.min(this.stations, distanceComp);
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		double minDist = end.distancePoint(stationsBis.get(0).getP());
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (s instanceof PlusStation && s.getP().distancePoint(end) <= 1.10*minDist) {
				stations[1] = s;
				break;
			} else if (s.getP().distancePoint(end) > 1.10*minDist) {
				break;
			}
		}
		return stations;
	}

	
	@Override
	public Station[] findPath(Point start, Point end) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start);
		stations[0] = Collections.min(this.stations, distanceComp);
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		stations[0] = Collections.min(this.stations, distanceComp);
		Collections.sort(stationsBis, distEndComp);
		double minDist = end.distancePoint(stationsBis.get(0).getP());
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (s instanceof PlusStation && s.getP().distancePoint(end) <= 1.10*minDist) {
				stations[1] = s;
				break;
			} else if (s.getP().distancePoint(end) > 1.10*minDist) {
				break;
			}
		}
		return stations;
	}

	
	@Override
	public Station findEndStation(Point start, Point end, Bike bike) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		DistanceEndComparator dec = new DistanceEndComparator(end);
		Collections.sort(stationsBis, dec);
		Station station = stationsBis.get(0);
		double minDist = end.distancePoint(station.getP());
		for (Station s : stationsBis) {
			if (s instanceof PlusStation && s.getP().distancePoint(end) <= 1.10*minDist) {
				station = s;
				break;
			} else if (s.getP().distancePoint(end) > 1.10*minDist) {
				break;
			}
		}
		return station;
	}

}
