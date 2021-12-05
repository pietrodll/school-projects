package sorting.station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import station.Station;

/**
 * This strategy sorts an {@code ArrayList} of {@code Station} w.r.t. the rate of occupation during a given period of time by using the {@code NumberOpComparator}. 
 * @author Chloé
 * @see SortingStrategy
 * @see OccRateComparator
 */
public class LeastOccupiedStation implements SortingStrategy {
	
	private LocalDateTime startDate = LocalDateTime.of(2019,  1, 1, 0, 0);
	private LocalDateTime endDate = LocalDateTime.of(2019, 12, 1, 0 ,0);
	
	public LeastOccupiedStation(LocalDateTime startDate, LocalDateTime endTime) {
		this.startDate = startDate;
		this.endDate = endTime;
	}
	
	@Override
	public ArrayList<Station> sorting(ArrayList<Station> s) {
		Collections.sort(s,new OccRateComparator(startDate, endDate));
		return s;
	}
	
	@Override
	public String toString() {
		return "Least Occupied";
	}

}
