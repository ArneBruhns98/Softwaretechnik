package util;

import java.awt.*;

public class PointOnLine {

    private int x;
    private int y;
    private Color color;


    public PointOnLine(int x, int y, Color color) {
        this.x  =x;
        this.y = y;
        this.color = color;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Color getColor(){
        return this.color;
    }
}
