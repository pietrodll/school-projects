package sorting.station;

import java.util.Comparator;
import station.Station;

/**
 * This class implements a {@code Comparator<Station>}. It compares the {@code Station} w.r.t. the number of operations.
 * @author Chloé
 * @see SortingStrategy
 * @see MoreUsedStation
 */
public class NumberOpComparator implements Comparator<Station> {
	
	/**
	 * Compares the {@code Station} w.r.t. the number of operations.
	 */
	@Override
	public int compare (Station s1, Station s2){
		return s1.getTotalOperations() - s2.getTotalOperations();
	}
	

}
