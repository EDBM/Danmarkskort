package BFST20Project;

import javafx.geometry.Point2D;

public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* Create a Point from a Point2D.
    * Point uses floats for internal representation. */
    public Point(Point2D point2D) {
        this((float) point2D.getX(), (float) point2D.getY());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
