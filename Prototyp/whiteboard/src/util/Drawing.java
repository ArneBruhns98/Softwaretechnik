package util;

import java.awt.Color;
import java.awt.Point;

/**
 * Klasse Zeichnung
 * @author Arne Bruhns
 */
public class Drawing extends Element {

	private Color color;

	private int thickness;

	private PointOnLine[] points;
	//Some comment

	public Drawing(Color color, int thickness, PointOnLine[] points, String user) {
		super(points[0].getX(), points[0].getY(), user);
		this.color = color;
		this.thickness = thickness;
		this.points = points;
	}
}
