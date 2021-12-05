package card;

import ride.Network;
import user.User;

/**
 * This class creates {@code Card} objects.
 * @author Pietro Dellino
 * @see Card
 */
public class CardFactory {
	
	public Network net;
	
	public CardFactory(Network net) {
		super();
		this.net = net;
	}

	public static final int VLIBRE = 1;
	public static final int VMAX = 2;
	public static final int CREDIT = 3;
	
	public Card createCard(int cardType, User user) {
		switch (cardType) {
		case VLIBRE:
			Card c1 = new VlibreCard(user);
			this.net.addCard(c1);
			return c1;
		case VMAX:
			Card c2 = new VmaxCard(user);
			this.net.addCard(c2);
			return c2;
		case CREDIT:
			Card c3 = new CreditCard(user);
			this.net.addCard(c3);
			return c3;
		default:
			return null;
		}
	}

}
