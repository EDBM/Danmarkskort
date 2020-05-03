package BFST20Project.Parser;

import BFST20Project.Point;

import java.io.Serializable;

public class OSMNode implements Serializable {
    private long id;
    private float lat, lon;

    public OSMNode(long id, float lat, float lon){
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public Point getPoint(){ return new Point(lat, lon); }

    public long getId(){return id;}
}
