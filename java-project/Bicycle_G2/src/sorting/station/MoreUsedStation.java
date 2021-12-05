package sorting.station;

import java.util.ArrayList;
import java.util.Collections;
import station.Station;

/**
 * This strategy sorts an {@code ArrayList} of {@code Station} w.r.t. the number of operations by using the {@code NumberOpComparator}. 
 * @author Chloé
 * @see SortingStrategy
 * @see NumberOpComparator
 */
public class MoreUsedStation implements SortingStrategy {

	@Override
	public ArrayList<Station> sorting(ArrayList<Station> s) {
		Collections.sort(s,new NumberOpComparator());
		Collections.reverse(s);
		return s;
	}
	
	@Override
	public String toString() {
		return "More Used";
	}
}
