package station;

import java.time.LocalDateTime;

import bike.Bike;
import tools.NegativeTimeException;

/**
 * This class represents a state of a {@code Slot} during a period of time. It has a beginning and ending time and gives the {@code Bike} parked in the {@code Slot} during the period, and whether the {@code Slot} is online. A {@code SlotState} can be created with or without an ending time.
 * @author Chloé
 *
 */
public class SlotState {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean isOnline;
	private Bike bike;
	
	
	public SlotState(LocalDateTime startTime, boolean isOnline, Bike bike) {
		super();
		this.startTime = startTime;
		this.isOnline = isOnline;
		this.bike = bike;
	}
	
	public SlotState(LocalDateTime startTime, LocalDateTime endTime, boolean isOnline, Bike bike) throws NegativeTimeException {
		super();
		if (endTime.isBefore(startTime)) {
			throw new NegativeTimeException(startTime, endTime);
		}
		this.startTime = startTime;
		this.endTime = endTime;
		this.isOnline = isOnline;
		this.bike = bike;
	}
	
	
	public LocalDateTime getStartTime() { return startTime; }

	public LocalDateTime getEndTime() { return endTime; }

	/**
	 * If the ending time of a {@code SlotState} is before its beginning time, an exception is thrown.
	 * @param endTime
	 * @throws NegativeTimeException
	 */
	public void setEndTime(LocalDateTime endTime) throws NegativeTimeException {
		if (endTime.isBefore(this.startTime)) {
			throw new NegativeTimeException(this.startTime, endTime);
		}
		this.endTime = endTime;
	}
	
	public boolean isOnline() { return isOnline; }

	public Bike getBike() {	return bike; }

	/**
	 * A {@code Slot} is occupied if it is offline or if it has a {@code Bike}.
	 * @return whether the {@code Slot} was occupied during this {@code SlotState}
	 */
	public boolean getisOccupied() {
		if (isOnline == false) {
			return true;
		}
		else if ( bike != null ) {
			return true;
		}
		else {return false;}
	}
	
	
}
