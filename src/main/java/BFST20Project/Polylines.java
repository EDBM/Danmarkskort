package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Polylines implements Drawable, Serializable {
    private Point[] coordinates;
    private WayType wayType;

    public Polylines(OSMWay way){
        wayType = way.getWayType();
        coordinates = new Point[way.size()];
        setWayType(way.getWayType());
        for(int i = 0; i < way.size(); i++){
            Point coordinate = new Point(0.56f * way.get(i).getLon(), -way.get(i).getLat());
            coordinates[i] = coordinate;
        }
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


    public List<Point> getCoordinates() {
        return Arrays.asList(coordinates);

    }

}
