package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;

import java.util.ArrayList;
import java.util.List;

public class RouteNode {
    OSMNode node;
    List<Edge> possiblePaths;

    public RouteNode(OSMNode node){
        this.node=node;
        possiblePaths = new ArrayList<Edge>();
    }

    public void addEdge(Edge edge){
        possiblePaths.add(edge);
    }

    public List<Edge> getPossiblePaths(){
        return possiblePaths;
    }

    public double getLon() {
        return node.getLon();
    }

    public double getLat() {
        return node.getLat();
    }
}
