package test.tools;

import org.junit.jupiter.api.Test;

import tools.Point;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
	
	@Test
	void testEqualPosition() {
		assertAll(
				() -> {
					Point p1 = new Point(2,9);
					Point p2 = new Point (2,9);
					assertTrue(p2.equals(p1));
				},
				() -> {
					Point p1 = new Point(0,8);
					Point p2 = new Point (2,8);
					assertFalse(p2.equals(p1));
				},
				() -> {
					Point p1 = new Point(0.5,8);
					Point p2 = new Point (0.5,8);
					assertTrue(p2.equals(p1));
				},
				() -> {
					Point p1 = new Point(6,8);
					Point p2 = new Point (6.0, 8);
					assertTrue(p2.equals(p1));		
				}
		);	
	}

	
	@Test 
	void testDistance() {
		assertAll(
				() -> {
					Point p1 = new Point(3,9);
					Point p2 = new Point (2,8);
					assertAll( "Calculate Distance",
							() -> assertEquals(Math.pow(2, 0.5), p2.distancePoint(p1)),
							() -> assertEquals(Math.pow(2, 0.5), p1.distancePoint(p2))
					);
				},
				() -> {
					Point p1 = new Point(-3,9);
					Point p2 = new Point (2,8);
					assertAll("With a negative int",
							() -> assertEquals(Math.pow(26, 0.5), p2.distancePoint(p1))
					);
				},
				() -> {
					Point p1 = new Point(3,9);
					Point p2 = new Point (3,9);
					assertAll("With same position",
							() -> assertEquals(0, p2.distancePoint(p1))
					);
				}
		);
	}

}
