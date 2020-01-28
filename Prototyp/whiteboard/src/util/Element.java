package util;

/**
 * Klasse Element
 * @author Arne Bruhns
 */
public abstract class Element {

	private int id;

	private double x;

	private double y;

	private String user;

	private static int countElements = 0; 			//Autoincrement id

	public Element(double x, double y, String user) {
		this.id = ++countElements;
		this.x = x;
		this.y = y;
		this.user = user;
	}

	public Element(){}

}
