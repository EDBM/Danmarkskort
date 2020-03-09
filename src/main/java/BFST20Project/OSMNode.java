package BFST20Project;

public class OSMNode {
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

    public long getId(){return id;}

    /*public String toString() {
        return "id = " + id + " lattitude = " + lat + " longtitude = " + lon;
    }*/
}
