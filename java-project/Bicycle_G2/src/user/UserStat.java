package user;

/**
 * This class regroups all the computed statistic of a {@code User} object. This class allows only to add to its attributes, there are no setters. 
 * @author Chloé
 * @see User
 */
public class UserStat {
	
	private int numberRides;
	private double totalTime;
	private double totalAmount;
	private double totalCreditEarned;
	
	/**
	 * The constructor of the class {@code UserStat} sets all parameters to 0.
	 */
	public UserStat() { 
		super();
		this.numberRides = 0;
		this.totalTime = 0;
		this.totalAmount = 0;
		this.totalCreditEarned = 0;
	}

	public int getNumberRides() { return numberRides; }

	public void addRide() { this.numberRides++;	}

	public double getTotalTime() { return totalTime; }

	public void addTime(double time) { this.totalTime += time;	}

	public double getTotalAmount() { return totalAmount; }

	public void addAmount(double amount) { this.totalAmount += amount; }	

	public double getTotalCreditEarned() { return totalCreditEarned; }

	public void addCreditEarned(double creditEarned) { this.totalCreditEarned += creditEarned;	}
	

}
