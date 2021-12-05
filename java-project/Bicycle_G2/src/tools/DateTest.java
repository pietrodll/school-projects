package tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class DateTest {
	
	@Test
	void testDateInput() {
		String user = "2019-11-23 12:54";
		LocalDateTime t1 = LocalDateTime.of(2019, 11, 23, 12, 54);
		assertEquals(t1, Date.dateInput(user) );
		
	}
	
	@Test
	void testSameDateInput() {
		String user = "2019-11-23 12:54";
		String user2 = "2019-11-23 12:54";
		assertEquals(Date.dateInput(user), Date.dateInput(user2));	
	}
	
	@Test
	void testWrongInput() {
		String user = "2019-11/23 12:54";
		assertThrows(DateTimeParseException.class,
				() -> {
					Date.dateInput(user);
				}
		);
	}

	@Test
	void testComputeTimeMinutes() throws NegativeTimeException, DateTimeParseException, NullDateException {
		String user = "2019-11-23 12:50";
		String user2 = "2019-11-23 12:54";
		assertEquals(4, Date.computeTime(Date.dateInput(user), Date.dateInput(user2)));	
	}
	
	@Test
	void testComputeTimeHour() throws NegativeTimeException, DateTimeParseException, NullDateException {
		String user = "2019-11-23 12:50";
		String user2 = "2019-11-23 13:54";
		assertEquals(64, Date.computeTime(Date.dateInput(user), Date.dateInput(user2)));	
	}
	
	@Test
	void testComputeTimeDay() throws NegativeTimeException, DateTimeParseException, NullDateException {
		String user = "2019-11-23 12:50";
		String user2 = "2019-11-24 13:54";
		assertEquals(1504, Date.computeTime(Date.dateInput(user), Date.dateInput(user2)));	
	}
	
	@Test
	void testComputeTimeSameTime() throws NegativeTimeException, DateTimeParseException, NullDateException {
		String user = "2019-11-23 12:44";
		String user2 = "2019-11-23 12:44";
		assertEquals(0, Date.computeTime(Date.dateInput(user), Date.dateInput(user2)));	
	}
	
	@Test
	void testComputeNullTime() throws NegativeTimeException, NullDateException {
		LocalDateTime t1 = LocalDateTime.of(2019,  11, 23, 12, 44);
		LocalDateTime t2 = null;
		assertThrows(NullDateException.class,
				() -> {
					Date.computeTime(t1, t2);
				}
		);
	}
	@Test
	void testComputeTimeNegativeTime() throws NegativeTimeException {
		String user = "2019-11-23 12:50";
		String user2 = "2019-11-23 12:44";
		assertThrows(NegativeTimeException.class,
				() -> {
					Date.computeTime(Date.dateInput(user), Date.dateInput(user2));
				}
		);
	}
	
	


}

