package BFST20Project.Routeplanner;

import java.util.*;

public class ShortestPath {
    double[] distTo;
    DirectedEdge[] edgeTo;

    DirectedGraph graph;

    Vertex endVertex;

    IndexMinPQ<Double> priorityQueue;

    Deque<DirectedEdge> path = new LinkedList<>();
    double totalWeight;

    boolean isDriving;

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

        int n = 0;
        for (double d : distTo) {
            if(d != Double.POSITIVE_INFINITY){
                n++;
            }
        }
        System.out.println("Vertices visited: " + n);
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

            if(isDriving && edge.isDrivable){
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
                    System.out.println("from id: " + from);
                    System.out.println("to id: " + to);

                    System.out.println("distTo[to]: " + distTo[to]);
                    System.out.println("heuristic(to): " + heuristic(to));
                    System.out.println(priorityQueue.keyOf(to));
                }
            else
                priorityQueue.insert(to, distTo[to] + heuristic(to));
        }
    }

    private double heuristic(int to) {
        Vertex toVertex = graph.getVertex(to);
        if(isDriving) return endVertex.getPoint().distanceTo(toVertex.getPoint())/DirectedGraph.MAX_DRIVE_SPEED;
        else return endVertex.getPoint().distanceTo(toVertex.getPoint());
    }

    public Deque<DirectedEdge> getPath() {
        return path;
    }

    public double getTotalWeight(){
        return totalWeight;
    }
}
