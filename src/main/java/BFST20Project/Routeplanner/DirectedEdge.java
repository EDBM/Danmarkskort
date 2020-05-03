package BFST20Project.Routeplanner;

import java.io.Serializable;
import BFST20Project.OSMNode;
import BFST20Project.Point;
import java.util.Objects;

public class DirectedEdge implements Serializable {
    private Vertex start;
    private Vertex end;
    private double length;
    private double speed;
    private String name;
    boolean isDriveable;
    boolean isWalkable;

    public DirectedEdge(Vertex start, Vertex end, double speed, String name, boolean isDriveable, boolean isWalkable){
        this.start = start;
        this.end = end;
        this.speed = speed;
        length = calculateLength();
        this.isDriveable = isDriveable;
        this.isWalkable = isWalkable;
        this.name = Objects.requireNonNullElse(name, "Ikke navngivet vej");

        start.addEdge(this);
    }

    private double calculateLength() {
        length = Point.distanceBetweenPoint(start.getPoint(),end.getPoint());
        return length;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() { return end; }
    

    public double getWeight(boolean isDriving) {
        if(isDriving) return length/speed;
        else return length;
    }

    public void setName(String name){this.name=name;}
    public String getName() throws NullPointerException{return name;}
    public double getLength(){return length;}


}