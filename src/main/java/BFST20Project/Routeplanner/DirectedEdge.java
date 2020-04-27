package BFST20Project.Routeplanner;

import java.io.Serializable;

public class DirectedEdge implements Serializable {
    private Vertex start;
    private Vertex end;
    private double length;
    private double speed;
    public boolean isDrivable;
    public boolean isWalkable;

    public DirectedEdge(Vertex start, Vertex end, double speed, boolean isDrivable, boolean isWalkable){
        this.start = start;
        this.end = end;
        this.speed = speed;
        length = calculateLength();

        //These are not mutually exclusive
        this.isDrivable = isDrivable;
        this.isWalkable = isWalkable;

        start.addEdge(this);
    }

    private double calculateLength() {
        length = start.getPoint().distanceTo(end.getPoint());
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
}