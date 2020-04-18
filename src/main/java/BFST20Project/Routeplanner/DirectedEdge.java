package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;

public class DirectedEdge {
    private Vertex start;
    private Vertex end;
    private double length;
    private double speed;

    public DirectedEdge(Vertex start, Vertex end, double speed){
        this.start = start;
        this.end = end;
        if (Double.isNaN(speed)) throw new IllegalArgumentException("Speed is Not a Number");
        this.speed = speed;
        length = calculateLength();

        start.addEdge(this);
    }

    private double calculateLength() {
        double x1 = start.getLat();
        double x2 = end.getLat();
        double y1 = start.getLon();
        double y2 = end.getLon();
        length = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
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
}