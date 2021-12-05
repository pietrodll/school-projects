package ride.path;

import java.util.ArrayList;
import java.util.Collections;

import bike.Bike;
import bike.BikeFactory;
import bike.ElectricBike;
import bike.MechanicBike;
import ride.Network;
import station.Slot;
import station.Station;
import tools.Point;

/**
 * This path strategy prefers a pickup station with more bikes available (depending on the type specified) and that is less
 * than 5% further than the closest to the start point. It prefers a return station that has more slots available and is less than
 * 5% further than the closest to the destination.
 * @author Pietro
 *
 */
public class UniformityStrategy implements PathStrategy {
	
	private ArrayList<Station> stations;
	
	public UniformityStrategy(Network net) {
		this.stations = net.getStations();
	}
	
	/**
	 * This method calculates the number of available bikes of a station. The value of {@code bikeType} has to be {@code BikeFactory.ELECTRIC} or {@code BikeFactory.MECHANIC} 
	 * @param s {@code Station}
	 * @param bikeType {@code int}
	 * @return The number of available bikes according to the {@code bikeType}.
	 * @see BikeFactory
	 */
	private static int availableBikes(Station s, int bikeType) {
		int n = 0;
		if (bikeType == BikeFactory.MECHANIC) {
			for (Slot slot : s.getParkingSlots()) {
				if (slot.isOnline() && (slot.getBike() instanceof MechanicBike)) { n++; }
			}
		} else if (bikeType == BikeFactory.ELECTRIC) {
			for (Slot slot : s.getParkingSlots()) {
				if (slot.isOnline() && (slot.getBike() instanceof ElectricBike)) { n++; }
			}
		}
		return n;
	}
	
	/**
	 * This method calculates the number of available bikes of a station. 
	 * @param s {@code Station}
	 * @return The number of available bikes.
	 */
	private static int availableBikes(Station s) {
		int n = 0;
		for (Slot slot : s.getParkingSlots()) {
			if (slot.isOnline() && slot.getisOccupied()) { n++; }
		}
		return n;
	}

	/**
	 * This method calculates the number of available slots of a station. A slot is available if it is online and has no bike.
	 * @param s
	 * @return The number of available slots.
	 * @see Slot#getisOccupied()
	 */
	private static int availableSlots(Station s) {
		int n = 0;
		for (Slot slot : s.getParkingSlots()) {
			if (!slot.getisOccupied()) {
				n++;
			}
		}
		return n;
	}
	
	
	@Override
	public Station[] findPath(Point start, Point end, int bikeType) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		Station[] stations = new Station[2];
		DistanceStartComparator distanceComp = new DistanceStartComparator(start, bikeType);
		Collections.sort(stationsBis, distanceComp);
		stations[0] = stationsBis.get(0);
		double startDist = start.distancePoint(stationsBis.get(0).getP());
		for (Station s : stationsBis) {
			if (availableBikes(s, bikeType) > availableBikes(stations[0], bikeType) && s.getP().distancePoint(start) <= 1.05*startDist) {
				stations[0] = s;
			} else if (s.getP().distancePoint(start) > 1.05*startDist) {
				break;
			}
		}
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		double endDist = end.distancePoint(stationsBis.get(0).getP());
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (availableSlots(s) > availableSlots(stations[1]) && s.getP().distancePoint(end) <= 1.05*endDist) {
				stations[1] = s;
			} else if (s.getP().distancePoint(end) > 1.05*endDist) {
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
		double startDist = start.distancePoint(stationsBis.get(0).getP());
		for (Station s : stationsBis) {
			if (availableBikes(s) > availableBikes(stations[0]) && s.getP().distancePoint(start) <= 1.05*startDist) {
				stations[0] = s;
			} else if (s.getP().distancePoint(start) > 1.05*startDist) {
				break;
			}
		}
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		double endDist = end.distancePoint(stationsBis.get(0).getP());
		stations[1] = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (availableSlots(s) > availableSlots(stations[1]) && s.getP().distancePoint(end) <= 1.05*endDist) {
				stations[1] = s;
			} else if (s.getP().distancePoint(end) > 1.05*endDist) {
				break;
			}
		}
		return stations;
	}

	@Override
	public Station findEndStation(Point start, Point end, Bike bike) {
		ArrayList<Station> stationsBis = (ArrayList<Station>) this.stations.clone();
		DistanceEndComparator distEndComp = new DistanceEndComparator(end);
		Collections.sort(stationsBis, distEndComp);
		double endDist = end.distancePoint(stationsBis.get(0).getP());
		Station station = stationsBis.get(0);
		for (Station s : stationsBis) {
			if (availableSlots(s) > availableSlots(station) && s.getP().distancePoint(end) <= 1.05*endDist) {
				station = s;
			} else if (s.getP().distancePoint(end) > 1.05*endDist) {
				break;
			}
		}
		return station;
	}

}
