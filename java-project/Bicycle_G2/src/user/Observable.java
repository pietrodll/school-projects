package user;

/**
 * This class enables to implement the notification system.
 * @author Chloé
 *
 */
public interface Observable {
	
	public void registerObserver(Observer observer);
	public void removeObserver(Observer osberver);
	public void notifyObservers();

}
