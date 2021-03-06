package tools;

import java.time.LocalDateTime;
/**
 * This exception is thrown when a date is supposed to be after another, and that it is not the case
 * @author Chlo?
 *
 */
public class NegativeTimeException extends Exception {
	
	/**
	 * Generated by Papyrus
	 */
	private static final long serialVersionUID = -1897800301626475809L;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public NegativeTimeException(LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}
	
	@Override
	public String getMessage() {
		return "Error : The starting date [" + startTime + "] is after the ending date [" + endTime + "].";
	}


}
