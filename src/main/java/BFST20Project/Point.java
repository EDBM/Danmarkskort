package BFST20Project;

import javafx.geometry.Point2D;

public class Point {

    private float x;
    private float y;

    public static Point fromLonLat(float lon, float lat){
        return new Point(0.56f * lon, -lat);
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* Create a Point from a Point2D.
    * Point uses floats for internal representation. */
    public Point(Point2D point2D) {
        this((float) point2D.getX(), (float) point2D.getY());
    }

    public double distanceTo(Point other){
        return Math.sqrt((this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y));
    }

    public float getCoord(char c){
        if(c == 'x')
            return x;
        else if(c == 'y')
            return y;
        throw new IllegalArgumentException(c + " is not a coordinate character");
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float distanceToLine(Point a, Point b) {
        float px = b.x - a.x;
        float py = b.y - a.y;
        float temp = (px * px) + (py * py);
        float u = ((this.x - a.x) * px + (this.y - a.y) * py) / (temp);
        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }
        float x = a.x + u * px;
        float y = a.y + u * py;

        float dx = x - this.x;
        float dy = y - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return (float) dist;
    }
}
