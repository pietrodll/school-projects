package station;

import java.time.LocalDateTime;

import card.Card;
import ride.Network;
import tools.NegativeTimeException;
import tools.NullDateException;
import tools.Point;

/**
 * This class represents Plus stations, it extends the class {@code Station}.
 * @author Chloé
 * @see Station
 */
public class PlusStation extends Station {
	
	public PlusStation (Point p, Network net) {
		super (p, net);
	}
	
	/**
	 * If a {@code User} drops a {@code Bike} in a {@code PlusStation}, he gains 5 Time Credits on his subscription Card (if he has one).
	 * @return 
	 * @throws NullDateException 
	 * @throws StationOfflineException 
	 * @throws NoOngoingRideException 
	 * @throws NoSlotAvailableException 
	 * @throws OngoingRideException 
	 */
	@Override
	public synchronized double dropBike(Card card, LocalDateTime dropTime) throws NegativeTimeException, NullDateException, NoSlotAvailableException, NoOngoingRideException, StationOfflineException, OngoingRideException {
		double price = super.dropBike(card, dropTime);
		card.addCredit(5);
		return price;
	}

	/**
	 * Redefinition of the equals() method
	 */
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof PlusStation) {
			PlusStation other = (PlusStation) obj;
			res = this.getId() == other.getId();
		}
		return res;
	}

}
