package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;


public class Polylines implements Drawable, Serializable {
    OSMWay way;
    private float xCoords, yCoords;

    public Polylines(OSMWay way){
        this.way=way;
        xCoords = way.get(0).getLon();
        yCoords = way.get(0).getLat();
    }

    public void stroke(GraphicsContext gc){
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }
    public void trace(GraphicsContext gc){
        gc.moveTo(way.get(0).getLon(), way.get(0).getLat());
        for (int i = 2; i<way.size() ; i=+2){
            gc.lineTo(way.get(i).getLon(), way.get(i).getLat());
        }
    }

    public void fill(GraphicsContext gc){
        gc.beginPath();
        trace(gc);
        gc.fill();
    }
}
