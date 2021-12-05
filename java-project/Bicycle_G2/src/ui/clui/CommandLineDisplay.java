package ui.clui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import bike.ElectricBike;
import card.Card;
import ride.Itinerary;
import ride.Network;
import sorting.station.SortingStrategy;
import station.NoSlotStateAtDateException;
import station.Slot;
import station.Station;
import tools.NegativeTimeException;
import tools.NullDateException;
import user.User;

/**
 * This class displays objects on the console.
 * @author Pietro Dellino
 */
public class CommandLineDisplay {
	
	
	public CommandLineDisplay() {
		super();
	}
	
	private String display(Station s, boolean show) {
		String disp = "";
		disp += "Station: id:" + s.getId() + '\n';
		float x = (float) (Math.round(s.getP().getX()*1000)/1000.0);
		float y = (float) (Math.round(s.getP().getY()*1000)/1000.0);
		disp += '\t' + "Position: x=" + x + " y=" + y + '\n';
		disp += "Slots: " + s.getParkingSlots().size() + '\n';
		for (Slot slot : s.getParkingSlots()) {
			disp += '\t' + "id:" + slot.getId() + '\t' + (slot.isOnline() ? "Online" : "Offline") +'\t' + (slot.getisOccupied() ? "Occupied" : "Free");
			if (slot.getisOccupied()) {
				disp += '\t' + "Bike id:" + slot.getBike().getId();
				String bt = slot.getBike() instanceof ElectricBike ? "Electric" : "Mechanic";
				disp += '\t' + "Type: " + bt;
			}
			disp += '\n';
		}
		if (show) System.out.println(disp);
		return disp;
	}
	
	public String display(Station s) {
		String disp = this.display(s, true);
		return disp;
	}
	
	private String display(Card c, boolean show) {
		String disp = "";
		disp += "Card: id:" + c.getId() + '\n';
		disp += '\t' + "Owner: " + c.getUser().getUserName() + '\n';
		disp += '\t' + "Type: " + c.getTypeString() + '\n';
		disp += '\t' + "Credit: " + c.getTimeCredit() + " minutes" + '\n';
		if (show) System.out.println(disp);
		return disp;
	}
	
	public String display(Card c) {
		String disp = this.display(c, true);
		return disp;
	}
	
	private String display(User u, boolean show) {
		double credit = Math.round(u.getUserStat().getTotalAmount()*100)/100.0;
		String disp = "";
		disp += "User: id:" + u.getId() + '\n';
		disp += '\t' + "Name: " + u.getUserName() + '\n';
		disp += '\t' + "Total amount paid: " + credit + " euros" + '\n';
		disp += '\t' + "Total number of rides: " + u.getUserStat().getNumberRides() + '\n';
		disp += '\t' + "Total ride time: " + Math.round(u.getUserStat().getTotalTime()) + " minutes" + '\n';
		disp += '\t' + "Total credit earned: " + Math.round(u.getUserStat().getTotalCreditEarned()) + " minutes" + '\n';
		if (show) System.out.println(disp);
		return disp;
	}
	
	public String display(User u) {
		String disp = this.display(u, true);
		return disp;
	}
	
	public String display(String s) {
		System.out.println(s);
		return s;
	}
	
	public String display(Network net) {
		String disp = "";
		disp += "Network: " + net.getName() + '\n' + "List of stations:" + '\n';
		for (Station s : net.getStations()) {
			disp += this.display(s, false);
		}
		disp += "List of cards:" + '\n';
		for (Card c : net.getCards()) {
			disp += this.display(c, false);
		}
		disp += "List of users:" + '\n';
		for (Card c : net.getCards()) {
			disp += this.display(c.getUser(), false);
		}
		System.out.println(disp);
		return disp;
	}
	
	private String display(Itinerary it, boolean show) {
		String disp = "";
		disp += "Pickup station:\n";
		disp += this.display(it.getStartStation(), false);
		disp += "Return station:\n";
		disp += this.display(it.getEndStation(), false);
		if (show) System.out.println(disp);
		return disp;
	}
	
	public String display(Itinerary it) {
		return this.display(it, true);
	}
	
	private String displayStationStat(Station s, LocalDateTime startTime, LocalDateTime endTime, boolean show) throws NoSlotStateAtDateException, NegativeTimeException, NullDateException {
		String disp = "";
		disp += "Station: id:" + s.getId() + '\n'
				+ '\t' + "Total rents: " + s.getTotalRents() + '\n'
				+ '\t' + "Total returns: " + s.getTotalReturns() + '\n'
				+ '\t' + "Total operations: " + s.getTotalOperations() + '\n'
				+ '\t' + "Occupation rate: " + s.getRateOccupation(startTime, endTime) + '\n';
		if (show) System.out.println(disp);
		return disp;
	}
	
	public String displayStationStat(Station s, LocalDateTime startTime, LocalDateTime endTime) throws NoSlotStateAtDateException, NegativeTimeException, NullDateException {
		return this.displayStationStat(s, startTime, endTime, true);
	}
	
	public String displaySortedStations(Network net, SortingStrategy strat, LocalDateTime startTime, LocalDateTime endTime) throws NoSlotStateAtDateException, NegativeTimeException, NullDateException {
		ArrayList<Station> sortedStations = net.sortingStations(strat);
		String disp = "Sorted stations of network " + net.getName() + " according to strategy " + strat.toString() + '\n';
		for (Station s : sortedStations) {
			disp += this.displayStationStat(s, startTime, endTime, false);
		}
		System.out.println(disp);
		return disp;
	}
	
}
