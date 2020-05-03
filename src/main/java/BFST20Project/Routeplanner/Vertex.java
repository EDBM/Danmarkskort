package BFST20Project.Routeplanner;

import BFST20Project.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vertex implements Serializable {
    private final int id;
    Point point;
    List<DirectedEdge> incidentEdges;

    public Vertex(Point node, int id){
        this.point = node;
        this.id = id;
        incidentEdges = new ArrayList<>();
    }

    public void addEdge(DirectedEdge edge){
        incidentEdges.add(edge);
    }

    public List<DirectedEdge> getIncidentEdges(){
        return incidentEdges;
    }

    public Point getPoint() {
        return point;
    }

    public double getLon() {
        return point.getX();
    }

    public double getLat() {
        return point.getY();
    }

    public int getId() {
        return id;
    }
}