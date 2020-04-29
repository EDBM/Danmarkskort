package BFST20Project.Routeplanner;

import BFST20Project.Point;

import java.util.*;

public class ShortestPath {
    double[] distTo;
    DirectedEdge[] edgeTo;

    DirectedGraph graph;

    Vertex endVertex;

    IndexMinPQ<Double> priorityQueue;

    Deque<DirectedEdge> path = new LinkedList<>();
    double totalWeight;

    public ShortestPath(DirectedGraph graph, int start, int end){
        this.graph = graph;
        this.endVertex = graph.getVertex(end);
        // this takes time proportional to graph size, no matter start and end.
        distTo = new double[graph.size()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        edgeTo = new DirectedEdge[graph.size()];
        priorityQueue = new IndexMinPQ<>(graph.size());

        distTo[start] = 0.0;

        priorityQueue.insert(start, 0.0);

        while(!priorityQueue.isEmpty()){
            int toRelax = priorityQueue.delMin();
            if(toRelax == end)
                break;
            relaxVertex(toRelax);
        }

        generatePath(end);
        textRoutePlanner();
    }

    private void generatePath(int end) {
        totalWeight = 0.0;
        DirectedEdge edge = edgeTo[end];
        while(edge != null){
            totalWeight += edge.getWeight();
            path.push(edge);
            edge = edgeTo[edge.getStart().getId()];
        }
    }

    private void relaxVertex(int toRelax) {
        for (DirectedEdge edge : graph.getIncidentEdges(toRelax)) {
            relaxEdge(edge);
        }
    }

    private void relaxEdge(DirectedEdge edge){
        int from = edge.getStart().getId();
        int to = edge.getEnd().getId();
        if (distTo[to] > distTo[from] + edge.getWeight()) {
            distTo[to] = distTo[from] + edge.getWeight();
            edgeTo[to] = edge;
            if (priorityQueue.contains(to))
                priorityQueue.decreaseKey(to, distTo[to] + heuristic(to));
            else
                priorityQueue.insert(to, distTo[to] + heuristic(to));
        }
    }

    private double heuristic(int to) {
        Vertex toVertex = graph.getVertex(to);
        return endVertex.getPoint().distanceTo(toVertex.getPoint())/DirectedGraph.MAX_DRIVE_SPEED;
    }

    public Deque<DirectedEdge> getPath() {
        return path;
    }

    public double getTotalWeight(){
        return totalWeight;
    }

    public ArrayList<String> textRoutePlanner() {
        ArrayList<String> textRoute = new ArrayList<>();
        String direction;
        int length = 0;
        double angle;
        DirectedEdge previousEdge = path.poll();
        String roadName = previousEdge.getName();

        for (DirectedEdge edge : path) {
            if(roadName!=null) {
                if (roadName.equals(edge.getName())) {
                    length += edge.getLength();
                    previousEdge = edge;
                } else if (!roadName.equals("")) {
                    angle = calculateTurnAngle(previousEdge, edge);
                    //angle = ((previousEdge.getEnd().getLon()-previousEdge.getStart().getLon())*(edge.getEnd().getLat()-previousEdge.getStart().getLat()))-
                    //      ((previousEdge.getEnd().getLat()-previousEdge.getStart().getLat())*(edge.getEnd().getLon()-previousEdge.getStart().getLon()));
                    System.out.println(angle);
                    if (angle >= 0 && angle <= 90) {
                        direction = " drej til højre";
                    } else if (angle >= 100 && angle <= 180) {
                        direction = " drej til venstre";
                    } else {
                        direction = " fortsæt ligeud";
                    }

                    textRoute.add("Følg " + roadName + " " + length + "m, derefter" + direction + " af " + edge.getName());
                    roadName = edge.getName();
                    length = 0;

                } else {
                    roadName = edge.getName();
                }
            }
        }

            textRoute.add("Du er nu ankommet");
        for(String s:textRoute){
            System.out.println(s);
        }
        return textRoute;
    }

    private double calculateTurnAngle(DirectedEdge a, DirectedEdge b) {
        double ay1 = a.getStart().getLon();
        double ax1 = a.getStart().getLat();
        double ay2 = a.getEnd().getLon();
        double ax2 = a.getEnd().getLat();
        double by1 = b.getStart().getLon();
        double bx1 = b.getStart().getLat();
        double by2 = b.getEnd().getLon();
        double bx2 = b.getEnd().getLat();

        double aLenght = Math.sqrt(Math.pow(Math.abs(ax1-ax2),2)+Math.pow(ay1-ay2,2));
        double bLength = Math.sqrt(Math.pow(Math.abs(ax1-bx2),2)+Math.pow(ay1-by2,2));
        double cLength = Math.sqrt(Math.pow(Math.abs(ax2-bx2),2)+Math.pow(ay2-by2,2));
        //double aLength = a.getLength();
        //double bLength = b.getLength();
        //double cLength = Point.distanceBetweenPoint(a.getStart().getPoint(),(b.getEnd().getPoint()));

        double turnAngle = Math.acos(aLenght*aLenght+bLength*bLength-cLength*cLength/(2*aLenght*bLength))*(180/Math.PI);
        /*
        double vectorABx = ax1 - bx1;
        double vectorABy = ay1 - by1;
        double vectorACx = ax1 - bx2;
        double vectorACy = ay1 - by2;

        double turnAngle = Math.acos((vectorABx*vectorACx+vectorABy*vectorACy)/(cLength))*(180/Math.PI);
        //double turnAngle = Math.acos((Math.pow(aLength,2)+Math.pow(cLength,2)-Math.pow(bLength,2))/(2*aLength*cLength));
        //turnAngle = turnAngle;
        */

        return turnAngle;
    }
}
