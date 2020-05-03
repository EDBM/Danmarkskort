package BFST20Project.Routeplanner;

import BFST20Project.Point;

import java.util.*;

public class ShortestPath {
    double[] distTo;
    DirectedEdge[] edgeTo;

    DirectedGraph graph;

    Vertex endVertex;
    boolean isDriving;

    IndexMinPQ<Double> priorityQueue;

    Deque<DirectedEdge> path = new LinkedList<>();
    double totalWeight;

    public ShortestPath(DirectedGraph graph, int start, int end, boolean isDriving){
        this.graph = graph;
        this.endVertex = graph.getVertex(end);
        this.isDriving = isDriving;
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
            totalWeight += edge.getWeight(isDriving);
            path.push(edge);
            edge = edgeTo[edge.getStart().getId()];
        }
    }

    private void relaxVertex(int toRelax) {
        for (DirectedEdge edge : graph.getIncidentEdges(toRelax)) {
            if(!isDriving && edge.isWalkable) {
                relaxEdge(edge);
            }

            if(isDriving && edge.isDriveable){
                relaxEdge(edge);
            }
        }
    }

    private void relaxEdge(DirectedEdge edge){
        int from = edge.getStart().getId();
        int to = edge.getEnd().getId();
        if (distTo[to] > distTo[from] + edge.getWeight(isDriving)) {
            distTo[to] = distTo[from] + edge.getWeight(isDriving);
            edgeTo[to] = edge;
            if (priorityQueue.contains(to))
                try {
                    priorityQueue.decreaseKey(to, distTo[to] + heuristic(to));
                } catch (IllegalArgumentException e){

                }
            else
                priorityQueue.insert(to, distTo[to] + heuristic(to));
        }
    }

    private double heuristic(int to) {
        Vertex toVertex = graph.getVertex(to);
        if (isDriving) return endVertex.getPoint().distanceTo(toVertex.getPoint()) / DirectedGraph.MAX_DRIVE_SPEED;
        else return endVertex.getPoint().distanceTo(toVertex.getPoint());
    }

    public Deque<DirectedEdge> getPath() {
        return path;
    }

    public double getTotalWeight(){
        return totalWeight;
    }

   public ArrayList<String> textRoutePlanner() {
        ArrayList<String> textRoute = new ArrayList<>();

        if(path.isEmpty()){
            textRoute.add("No route available");
            return textRoute;
        }

        String direction;
        int length = 0;
        double angle;
        String roadName;
        DirectedEdge previousEdge = path.poll();
        roadName = previousEdge.getName();
        System.out.println("ny");

        if(previousEdge.getName()!=null) {
            for (DirectedEdge edge : path) {
                if (roadName.equals(edge.getName())) {
                    length += edge.getLength();
                    previousEdge = edge;
                } else if (!roadName.equals("")) {
                    angle = calculateTurnAngle(previousEdge,edge);

                    if (angle > 0) {
                        direction = " drej til højre";
                    } else if (angle < 0) {
                        direction = " drej til venstre";
                    } else {
                        direction = " fortsæt ligeud";
                    }
                    System.out.println(angle);
                    textRoute.add("Følg " + roadName + " " + length + "m, Derefter" + direction + " af " + edge.getName());
                    length= (int) Math.floor(edge.getLength());
                    roadName = edge.getName();

                } else {
                    roadName = edge.getName();
                }
            }

            textRoute.add("Du er nu ankommet");
        }
        return textRoute;
    }

    private double calculateTurnAngle(DirectedEdge a, DirectedEdge b) {
        double ax1 = a.getStart().getLon();
        double ay1 = a.getStart().getLat();
        double ax2 = a.getEnd().getLon();
        double ay2 = a.getEnd().getLat();
        double bx2 = b.getEnd().getLon();
        double by2 = b.getEnd().getLat();

        double turnAngle = ((ax2-ax1)*(by2-ay1))-(ay2-ay1)*(bx2-ax2);

        return turnAngle;
    }
}
