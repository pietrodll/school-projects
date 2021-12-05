package ride.path;

import bike.Bike;
import station.Station;
import tools.Point;

/**
 * This interface contains methods to calculate the best pickup station and return station.
 * @author Pietro Dellino
 *
 */
public interface PathStrategy {
	
	/**
	 * This method returns a 2-element array with the start {@code Station} and the end {@code Station}, according to the start and the end of the itinerary, and the type of bike the user wants to use.
	 * @param start The {@code Point} from which the itinerary begins
	 * @param end The {@code Point} which is the destination of the itinerary
	 * @param bikeType An {@code int} representing the type of bike the user wants to use. The value has to be one of the constants from {@link bike.BikeFactory} ({@code ELECTRIC} and {@code MECHANIC}).
	 * @return A 2-element array with the start station and the end station.
	 */
	public Station[] findPath(Point start, Point end, int bikeType);
	
	/**
	 * This method returns a 2-element array with the start {@code Station} and the end {@code Station}, assuming that the user could take any type of bike.
	 * @param start ({@code Point}) The point from which the itinerary begins
	 * @param end ({@code Point}) The destination of the itinerary
	 * @return A 2-element array with the start station and the end station.
	 */
	public Station[] findPath(Point start, Point end);
	
	/**
	 * This method finds the best station to drop the bike, assuming that the user is currently riding it. It is useful when the initial end station (computed at the beginning of the ride) becomes unavailable. It is also usefull is a user did not chose to follow an itinerary before starting his ride.
	 * @param start The {@code Point} which is the current position of the user
	 * @param end The {@code Point} which is the destination of the itinerary
	 * @param bike The bike the user wants to drop
	 * @return The best {@code Station} for the user to drop his bike
	 */
	public Station findEndStation(Point start, Point end, Bike bike);

	
}
