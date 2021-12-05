package tools;

/**
 * This class represents locations on the map using simple GPS coordinates (x,y)
 * @author Chloé
 *
 */
public class Point {
	
	private double x;
	private double y;
	
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}


	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}
	
	/**
	 * 
	 * @param p
	 * @return the distance between this Point and a Point p
	 */
	public double distancePoint (Point p) {
		double x1 = this.getX();
		double y1 = this.getY();
		double x2 = p.getX();
		double y2 = p.getY();
		return Math.pow(Math.pow( x2 - x1 , 2 ) + Math.pow( y2 - y1 , 2 ), 0.5);
	}
	
	/**
	 * Redefinition of the equals() method in for class Point
	 */
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof Point) {
			Point p1 = (Point) obj;
			return (p1.getX() == this.getX()) && (p1.getY() == this.getY());
		}
		return false;
	}


	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	

}
