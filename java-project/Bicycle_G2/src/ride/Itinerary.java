package ride;


import ride.path.PathStrategy;
import station.Station;
import tools.Point;


/**
 * This class represents an itinerary for the users.
 * An {@code Itinerary} object is created with only a start point and an end point, and then the method
 * {@code computePath } affects values to the attributes {@code startStation} and {@code endStation}. 
 * @author Pietro Dellino
 */
public class Itinerary{
	
	private Point start;
	private Point end;
	private Station startStation;
	private Station endStation;
	private PathStrategy ps;
	
	public Itinerary(Point start, Point end) {
		this.start = start;
		this.end = end;
	}
	
	public Point getStart() { return start; }
	public Point getEnd() { return end; }
	public Station getStartStation() { return startStation; }
	public Station getEndStation() { return endStation; }
	public PathStrategy getPs() { return ps; }

	/**
	 * This method is used when the ending {@code Station} has to be changed because the initial ending {@code Station} is full. 
	 * @param endStation
	 */
	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}

	/**
	 * This method affects a value to {@code startStation} and {@code endStation} according to the
	 * network (which gives the availability of the bikes) and the strategy of choice of the stations.
	 * @param ps An object implementing the {@code PathStrategy} interface, which gives the way of
	 * choosing the stations.
	 * @see PathStrategy
	 */
	public void computePath(PathStrategy ps) {
		Station[] stations = ps.findPath(this.start, this.end);
		this.startStation = stations[0];
		this.endStation = stations[1];
		this.ps = ps;
	}
	
	/**
	 * This method affects a value to {@code startStation} and {@code endStation} according to the
	 * network (which gives the availability of the bikes) and the strategy of choice of the stations.
	 * @param ps An object implementing the {@code PathStrategy} interface, which gives the way of
	 * choosing the stations.
	 * @param bikeType An integer which corresponds to the type of the bike. The values have to be taken
	 * from {@code ELECTRIC} and {@code MECHANIC} constants from {@link bike.BikeFactory}.
	 * @see bike.BikeFactory
	 * @see PathStrategy
	 */
	public void computePath(PathStrategy ps, int bikeType) {
		Station[] stations = ps.findPath(this.start, this.end, bikeType);
		this.startStation = stations[0];
		this.endStation = stations[1];
		this.ps = ps;
	}
	
	

}
