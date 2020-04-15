package BFST20Project.Routeplanner;

import BFST20Project.Point;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private final int id;

    Point node;
    List<DirectedEdge> incidentEdges;
    public Vertex(Point node, int id){
        this.node=node;
        this.id = id;
        incidentEdges = new ArrayList<DirectedEdge>();
    }

    public void addEdge(DirectedEdge edge){
        incidentEdges.add(edge);
    }

    public List<DirectedEdge> getIncidentEdges(){
        return incidentEdges;
    }

    public double getLon() {
        return node.getX();
    }

    public double getLat() {
        return node.getY();
    }

    public int getId() {
        return id;
    }
}