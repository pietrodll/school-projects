package station;

import java.time.LocalDateTime;
import java.util.ArrayList;

import bike.Bike;
import tools.Date;
import tools.NegativeTimeException;
import tools.NullDateException;


/**
 * A class representing parking slots. When a {@code Slot} is created, it has a unique ID, is online and has no {@code Bike}.
 * @author Chloé
 * @see SlotState
 * @see Station
 */
public class Slot {
	
	private Station s;
	private int id;
	private boolean isOnline;
	private Bike bike;
	private ArrayList<SlotState> slotHistory;
	
	
	public Slot(Station s) {
		this.s = s;
		id = SlotIdGenerator.getInstance().getSlotID(s);
		bike = null;
		isOnline = true;
		slotHistory = new ArrayList<SlotState>();
	}


	/**
	 * This method allows to find the {@code SlotState} of a {@code Slot} at a certain time t, using its index in the SlotHistory list.
	 * <br> If t is during a {@code SlotState}, this {@code SlotState} will be returned.
	 * <br> If t is at a changing time of {@code SlotState}, then the {@code SlotState} that starts at t will be returned.
	 * <br> If t is before the first {@code SlotState}, it means the {@code Slot} was not created at that time, it will throw an exception.
	 * <br>If t is after the beginning of the current {@code SlotState}, the current {@code SlotState} will be returned.
	 * @param t
	 * @return index of {@code SlotState} of time t in the SlotHistory
	 * @throws NoSlotStateAtDateException
	 */
	public int indexSlotState (LocalDateTime t) throws NoSlotStateAtDateException{
		int i;
		int size = this.slotHistory.size();
		if (size == 1 || size == 0) return 0;
		for (i=0; i <= size-2; i ++) {
			if (slotHistory.get(i).getStartTime().isBefore(t) && slotHistory.get(i).getEndTime().isAfter(t)){
				return i;
			}
			if (t.equals(slotHistory.get(i).getStartTime())) {
				return i;
			}
		}
		if (t.equals(slotHistory.get(size-1).getStartTime())) {
			return i;
		}
		SlotState a = slotHistory.get(size-1);
		if (t.isAfter(a.getStartTime())){
			return size - 1;
		} else {
			throw new NoSlotStateAtDateException(t);	
		}
	}
	
	/**
	 * This method calculates the occupation time of a {@code Slot} between startTime and endTime by using its {@code SlotState} history.
	 * @param startTime
	 * @param endTime
	 * @return the time the {@code Slot} was occupied during startTime and endTime
	 * @throws NegativeTimeException
	 * @throws NoSlotStateAtDateException
	 * @throws NullDateException
	 */
	public int computeOccupationTime (LocalDateTime startTime, LocalDateTime endTime) throws NegativeTimeException, NoSlotStateAtDateException, NullDateException {
		if (this.slotHistory.size() == 0) return 0;
		int totalOccupationTime = 0;
		int iStart;
		int iEnd;
		iEnd = indexSlotState (endTime);
		try {
			iStart = indexSlotState (startTime);
		} catch (NoSlotStateAtDateException e) {
			iStart = 0;
		}
		for (int i = iStart; i <= iEnd; i++) {
			SlotState state = slotHistory.get(i);
			if (state.getisOccupied() == true) {
				if (state.getEndTime() == null) { 
					totalOccupationTime += Date.computeTime (state.getStartTime(), endTime);	
				}
				else {totalOccupationTime += Date.computeTime (state.getStartTime(), state.getEndTime());
				}
			}			
		}
		return totalOccupationTime;
	}
	
		
	/**
	 * A {@code Slot} is occupied if it is offline or if it has a {@code Bike}.
	 * @return whether the {@code Slot} is occupied
	 */
	public boolean getisOccupied() {
		if (isOnline == false) {
			return true;
		} else if ( bike != null ) {
			return true;
		} else {return false;}
	}
	
	
	public boolean isOnline() { return isOnline; }
	
	/**
	 * This method sets the boolean isOnline. When an isOnline changes, a new {@code SlotState} has to be created
	 * @param isOnline
	 * @param changeTime
	 * @throws NegativeTimeException
	 */
	public void setOnline(boolean isOnline, LocalDateTime changeTime) throws NegativeTimeException {
		boolean wasSlotOnline = this.isOnline();
		if (isOnline != this.isOnline) {
			this.isOnline = isOnline;
			if (slotHistory.size()!= 0) {
				SlotState lastState = slotHistory.get(slotHistory.size()-1);
				lastState.setEndTime (changeTime);}
			SlotState newState = new SlotState (changeTime, isOnline, this.getBike());
			slotHistory.add(newState);
			if (wasSlotOnline == true && this.s.isStationFull() == true){
				this.s.setChanged(true);
				this.s.notifyObservers();
			}
		}
	}
	
	public Bike getBike() { return bike; }
	
	/**
	 * This method sets the {@code Bike} of the {@code Slot}. When the {@code Bike} changes, a new {@code SlotState} has to be created.
	 * @param bike
	 * @param changeTime
	 * @throws NegativeTimeException
	 */
	public void setBike(Bike bike, LocalDateTime changeTime) throws NegativeTimeException {
		boolean wasStationFull = this.s.isStationFull();
		if (bike != this.bike) {
			this.bike = bike;
			if (slotHistory.size()!= 0) {
				SlotState lastState = slotHistory.get(slotHistory.size()-1);
				lastState.setEndTime(changeTime);}
			SlotState newState = new SlotState (changeTime, this.isOnline(), bike);
			slotHistory.add(newState);
			if (wasStationFull == false && this.s.isStationFull() == true){
				this.s.setChanged(true);
				this.s.notifyObservers();
			}
		}
	}
	

	public ArrayList<SlotState> getSlotHistory() { return slotHistory; }

	public Station getS() { return s; }

	public int getId() { return id; }
	
	
	/**
	 * Redefinition of the equals() method 
	 */
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof Slot) {
			Slot other = (Slot) obj;
			res = this.getId() == other.getId();
		}
		return res;
	}	

	@Override
	public String toString() {
		return "Slot" + this.getId();
	}
}

