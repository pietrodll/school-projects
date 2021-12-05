package sorting.station;

import java.util.ArrayList;
import station.Station;

/**
 * This interface implements the different strategies for sorting stations.
 * @author Chloé
 *
 */

public interface SortingStrategy{
	
	ArrayList<Station> sorting(ArrayList<Station> s);
}
