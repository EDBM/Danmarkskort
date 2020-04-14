package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;

public class Edge {
    private RouteNode startNode;

    public RouteNode getEndNode() {
        return endNode;
    }

    private RouteNode endNode;
    private double distance;
    private int speedLimit;
    private double cost;

    public Edge(RouteNode startNode, RouteNode endNode){
        this.startNode=startNode;
        this.endNode=endNode;
        distance = calculateDistance();
    }

    private double calculateDistance() {
        double x1 = startNode.getLat();
        double x2 = endNode.getLat();
        double y1 = startNode.getLon();
        double y2 = endNode.getLon();
        distance = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        return distance;
    }

    public double getDistance(){return distance;}
}