package ride.path;

import java.util.ArrayList;
import java.util.Collections;

import bike.Bike;
import bike.BikeFactory;
import ride.Network;
import station.Station;
import tools.Point;

/**
 * This {@code PathStrategy} minimizes the ride time. The return station is chosen in order to minimize the walking distance to the destination, and the pickup station is chosen in order to minimize the walking + riding time from the start point to the return station.
 * @author Pietro Dellino
 * @see PathStrategy
 * @see FastestPathComparator
 */
public class FastestPathStrategy implements PathStrategy {
	
	private ArrayList<Station> stations;

	public FastestPathStrategy(Network net) {
		this.stations = net.getStations();
	}

	@Override
	public Station[] findPath(Point start, Point end, int bikeType) {
		DistanceEndComparator dec = new DistanceEndComparator(end);
		Station returnStation = Collections.min(this.stations, dec);
		FastestPathComparator fpc = new FastestPathComparator(start, returnStation.getP(), bikeType);
		Station pickupStation = Collections.min(this.stations, fpc);
		Station[] stations = {pickupStation, returnStation};
		return stations;
	}

	@Override
	public Station[] findPath(Point start, Point end) {
		Station[] stElec = this.findPath(start, end, BikeFactory.ELECTRIC);
		Station[] stMech = this.findPath(start, end, BikeFactory.MECHANIC);
		Point startMech = stMech[0].getP(), endMech = stMech[1].getP();
		Point startElec = stElec[0].getP(), endElec = stElec[1].getP();
		double timeMechanic = startMech.distancePoint(endMech)/FastestPathComparator.MECHANIC_SPEED + (start.distancePoint(startMech) + end.distancePoint(endMech))/FastestPathComparator.WALKING_SPEED;
		double timeElec = endElec.distancePoint(startElec)/FastestPathComparator.ELECTRIC_SPEED + (start.distancePoint(startElec) + end.distancePoint(endElec))/FastestPathComparator.WALKING_SPEED;
		return timeElec <= timeMechanic ? stElec : stMech;
	}

	@Override
	public Station findEndStation(Point start, Point end, Bike bike) {
		DistanceEndComparator dec = new DistanceEndComparator(end);
		Station station = Collections.min(this.stations, dec);
		return station;
	}

}
