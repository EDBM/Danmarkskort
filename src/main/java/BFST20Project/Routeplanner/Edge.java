package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;

public class Edge {
    private RouteNode startNode;

    public RouteNode getEndNode() {
        return endNode;
    }

    private RouteNode endNode;
    private double length;
    private int speedLimit;
    private double weight;

    public Edge(RouteNode startNode, RouteNode endNode){
        this.startNode=startNode;
        this.endNode=endNode;
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is Not a Number");

        //this.weight = weight;
        length = calculateLength();
    }

    private double calculateLength() {
        double x1 = startNode.getLat();
        double x2 = endNode.getLat();
        double y1 = startNode.getLon();
        double y2 = endNode.getLon();
        length = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        return length;
    }

    public double getLength(){return length;}

    private void setWeight(float cost, float length){
        weight=cost+length;
    }
}