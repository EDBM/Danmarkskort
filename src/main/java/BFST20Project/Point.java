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

    public float distanceSquared(Point other){
        return (this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y);
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

    public static double distanceBetweenPoint(Point start, Point end){
        int R = 6371; //earth radius in km
        float startLat = start.getY()*-1;
        float startLon = start.getX()/0.56f;
        float endLat = end.getY()*-1;
        float endLon = end.getX()/0.56f;
        float deltaLat = Math.abs(startLat-endLat);
        float deltaLon = Math.abs(startLon-endLon);
        //Haversine formula
        double a = Math.pow(Math.sin(deltaLat/2),2)+Math.cos(startLat)*Math.cos(endLat)*Math.pow(Math.sin(deltaLon/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = R * c;
        d = d*1000; //converts to meters

        return d;
    }
}
