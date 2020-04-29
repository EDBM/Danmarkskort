package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;
import BFST20Project.Point;

public class DirectedEdge {
    private Vertex start;
    private Vertex end;
    private double length;
    private double speed;
    private String name;
    boolean isDriveable;
    boolean isWalkable;

    public DirectedEdge(Vertex start, Vertex end, double speed,String name, boolean isDriveable, boolean isWalkable){
        this.start = start;
        this.end = end;
        if (Double.isNaN(speed)) throw new IllegalArgumentException("Speed is Not a Number");
        this.speed = speed;
        length = calculateLength();
        this.name=name;
        this.isDriveable = isDriveable;
        this.isWalkable = isWalkable;

        start.addEdge(this);
    }

    private double calculateLength() {
        length= Point.distanceBetweenPoint(start.getPoint(),end.getPoint());
        //length = start.getPoint().distanceTo(end.getPoint());
        return length;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public double getWeight() {
        return length/speed;
    }
    public String getName(){return name;}
    public double getLength(){return length;}


}