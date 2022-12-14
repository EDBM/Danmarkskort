package BFST20Project;

import javafx.geometry.Point2D;
import java.io.Serializable;

public class Point implements Serializable {

    private float x,y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* Create a Point from a Point2D.
     * Point uses floats for internal representation. */
    public Point(Point2D point2D) {
        this((float) point2D.getX(), (float) point2D.getY());
    }

    public static Point fromLonLat(float lon, float lat){
        return new Point(0.56f * lon, -lat);
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

    //Credit to https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment by 'quano'
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

    //Credit to https://www.movable-type.co.uk/scripts/latlong.html
    public static double distanceBetweenPoint(Point start, Point end){
        int R = 6371; //earth radius in km
        float startLat = start.getX()/0.56f;
        float startLon = start.getY()*-1;
        float endLat = end.getX()/0.56f;
        float endLon = end.getY()*-1;
        double deltaLat = startLat*Math.PI/180-endLat*Math.PI/180;
        double deltaLon = startLon*Math.PI/180-endLon*Math.PI/180;
        //Haversine formula
        double a = Math.pow(Math.sin(deltaLat/2),2)+Math.cos(startLat*Math.PI/180)*Math.cos(endLat*Math.PI/180)*Math.pow(Math.sin(deltaLon/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = R * c;
        d = d*1000; //converts to meters

        return d;
    }
}
