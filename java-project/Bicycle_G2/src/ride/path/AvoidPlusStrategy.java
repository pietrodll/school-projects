package ride.path;

import java.util.ArrayList;
import java.util.Collections;

import bike.Bike;
import ride.Network;
import station.PlusStation;
import station.Station;
import tools.Point;

/**
 * This {@code PathStrategy} avoids {@code PlusStations}.
 * @see PathStrategy
 * @author Pietro Dellino
 */
public class AvoidPlusStrategy implements PathStrategy {
	
	private ArrayList<Station> stations;

	public AvoidPlusStrategy(Network net) {
		this.stations = net.getStations();
	}

	@Override
	public Station[] findPath(Point start, Point end, int bikeType) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start, bikeType);
		Collections.sort(stationsBis, distanceComp);
		stations[0] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (!(s instanceof PlusStation)) {
				stations[0] = s;
				break;
			}
		}
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (!(s instanceof PlusStation)) {
				stations[1] = s;
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
		Collections.sort(stationsBis, distanceComp);
		stations[0] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (!(s instanceof PlusStation)) {
				stations[0] = s;
				break;
			}
		}
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (!(s instanceof PlusStation)) {
				stations[1] = s;
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
		for (Station s : stationsBis) {
			if (!(s instanceof PlusStation)) {
				station = s;
				break;
			}
		}
		return station;
	}

}
