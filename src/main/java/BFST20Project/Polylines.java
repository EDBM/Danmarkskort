package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;


public class Polylines implements Drawable, Serializable {
    private float[][] coordinates;
    private WayType wayType;

    public Polylines(OSMWay way){
        coordinates = new float[way.size()][2];
        setWayType(way.getWayType());
        for(int i = 0; i < way.size(); i++){
            float[] coordinate = new float[2];
            coordinate[0] = 0.56f * way.get(i).getLon();
            coordinate[1] = -way.get(i).getLat();
            coordinates[i] = coordinate;
        }
    }

    public void stroke(GraphicsContext gc){
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }

    public void trace(GraphicsContext gc){
        float[] startCoord = coordinates[0];
        gc.moveTo(startCoord[0], startCoord[1]);
        for (int i = 1; i<coordinates.length ; i++){
            gc.lineTo(coordinates[i][0], coordinates[i][1]);
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
}
