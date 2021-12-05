package tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;


/**
 * This class is used to simplify the functions of  the {@code LocalDateTime} objects in the program
 * @author Chloé
 * @see LocalDateTime
 */

public class Date {
	
	/**
	 * The formatter that will be used throughout the program
	 */
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	

	/**
	 * This method calculates the time between two {@code LocalDateTime} objects, and returns an {@code int} number of minutes
	 * 
	 * @param startTime ({@code LocalDateTime})
	 * @param endTime ({@code LocalDateTime})
	 * @return the time ({@code int}) between {@code startTime} and {@code endTime}
	 * @throws NegativeTimeException
	 * @throws NullDateException
	 */
	public static int computeTime (LocalDateTime startTime, LocalDateTime endTime) throws NegativeTimeException, NullDateException {
		if (startTime== null || endTime == null){
			throw new NullDateException();
		}
		else {
			int timeSpend = (int) startTime.until (endTime, ChronoUnit.MINUTES);
			if (timeSpend < 0) {
				throw new NegativeTimeException(startTime, endTime);
			}
			else {return timeSpend;}
		}
	}

	
	/**
	 * This method converts a {@code String} into the corresponding {@code LocalDateTime} object 
	 * @param userInput
	 * @return a {@code LocalDateTime} object
	 */
	public static LocalDateTime dateInput(String userInput) throws DateTimeParseException {
		LocalDateTime formatDateTime = LocalDateTime.parse(userInput, formatter);
			return (formatDateTime);
	}
	
	

}