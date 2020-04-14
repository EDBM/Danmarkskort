package BFST20Project.Routeplanner;

import BFST20Project.Point;

import java.util.ArrayList;
import java.util.List;

public class RouteNode {
    Point node;
    List<Edge> incidentEdges;

    public RouteNode(Point node){
        this.node=node;
        incidentEdges = new ArrayList<Edge>();
    }

    public void addEdge(Edge edge){
        incidentEdges.add(edge);
    }

    public List<Edge> getIncidentEdges(){
        return incidentEdges;
    }

    public double getLon() {
        return node.getX();
    }

    public double getLat() {
        return node.getY();
    }
}