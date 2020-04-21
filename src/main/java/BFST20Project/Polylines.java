package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Polylines implements Drawable, Serializable {
    private Point[] coordinates;
    private Point minPoint, maxPoint;
    private WayType wayType;

    public Polylines(OSMWay way){
        wayType = way.getWayType();
        coordinates = new Point[way.size()];
        setWayType(way.getWayType());
        for(int i = 0; i < way.size(); i++){
            Point coordinate = Point.fromLonLat(way.get(i).getLon(), way.get(i).getLat());
            coordinates[i] = coordinate;
        }

        setMinMax();
    }


    public Polylines(Point[] coordinates, WayType wayType){
        this.coordinates = coordinates;
        this.wayType = wayType;
        setMinMax();
    }

    private void setMinMax(){
        float minX = coordinates[0].getX();
        float minY = coordinates[0].getY();
        float maxX = minX;
        float maxY = minY;

        for(int i = 1; i < coordinates.length; i++){
            if(coordinates[i].getX() < minX)
                minX = coordinates[i].getX();
            else if(coordinates[i].getX() > maxX)
                maxX = coordinates[i].getX();
            if(coordinates[i].getY() < minY)
                minY = coordinates[i].getY();
            else if(coordinates[i].getY() > maxY)
                maxY = coordinates[i].getY();
        }
        minPoint = new Point(minX, minY);
        maxPoint = new Point(maxX, maxY);
    }

    public void stroke(GraphicsContext gc){
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }

    public void trace(GraphicsContext gc){
        Point startCoord = coordinates[0];
        gc.moveTo(startCoord.getX(), startCoord.getY());
        for (int i = 1; i<coordinates.length ; i++){
            gc.lineTo(coordinates[i].getX(), coordinates[i].getY());
        }
    }

    public void fill(GraphicsContext gc){
        gc.beginPath();
        trace(gc);
        gc.fill();
    }

    public WayType getWayType() {
        return wayType;
    }

    @Override
    public void setWayType(WayType type) {
        this.wayType = wayType;
    }

    @Override
    public float getMaxX() {
        return maxPoint.getX();
    }

    @Override
    public float getMinX() {
        return minPoint.getX();
    }

    @Override
    public float getMaxY() {
        return maxPoint.getY();
    }

    @Override
    public float getMinY() {
        return minPoint.getY();
    }


    public List<Point> getCoordinates() {
        return Arrays.asList(coordinates);

    }

}
