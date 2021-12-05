package sorting.station;

import java.time.LocalDateTime;
import java.util.Comparator;
import station.NoSlotStateAtDateException;
import station.Station;
import tools.NegativeTimeException;
import tools.NullDateException;

/**
 * This class implements a {@code Comparator<Station>}. It compares the {@code Station} w.r.t the rate occupation during a given period of time.
 * @author Chloé
 * @see SortingStrategy
 * @see LeastOccupiedStation
 */
public class OccRateComparator implements Comparator<Station> {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
 
	public OccRateComparator(LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Compares {@code Station} w.r.t. the rate occupation
	 */
	@Override
	public int compare(Station s1, Station s2) {			
			LocalDateTime startDate = this.startTime;
			LocalDateTime endDate = this.endTime;			
			try {
				double s1occup = s1.getRateOccupation(startDate, endDate);
				double s2occup = s2.getRateOccupation(startDate, endDate);
				if (s1occup - s2occup > 0) {
					return 1;
				} else if (s1occup-s2occup < 0) {
					return -1;
				} else {
					return 0;
				}
			} catch (NoSlotStateAtDateException e) {
				return 0;
			} catch (NegativeTimeException e) {
				return 0;
			} catch (NullDateException e) {
				return 0;
			}
			
			

	}

}
