package BFST20Project.Routeplanner;

import java.io.Serializable;
import BFST20Project.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DirectedGraph implements Serializable {
    public static final int MAX_DRIVE_SPEED = 130;
    private final List<Vertex> vertices;
    private final List<DirectedEdge> edges;

    public DirectedGraph(List<Vertex> vertices, List<DirectedEdge> edges){
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(int id){
        return vertices.get(id);
    }

    public List<DirectedEdge> getEdges() {
        return edges;
    }

    public int size() {
        return vertices.size();
    }

    public Iterable<? extends DirectedEdge> getIncidentEdges(int vertex) {
        return vertices.get(vertex).getIncidentEdges();
    }

    /*
     * Given a Point and a starting Vertex, find a Vertex close to the Point.
     * The method works by examining vertices adjacent to the Starting Vertex, and, if their closer to the Point
     * consider that Vertex instead. This find the closest Vertex from the point, if there exists a path from the
     * Starting Vertex to the closest Vertex, where every Vertex in the path is closer to the Point than the previous.
     * This is done for performance reasons, so as to not examine the entire Graph.
     */
    public Vertex nearestVertex(Vertex startingVertex, Point point){
        double minDist = Double.POSITIVE_INFINITY;
        Queue<Vertex> potentiallyCloser = new LinkedList<>();
        Vertex closeVertex = startingVertex;

        potentiallyCloser.add(startingVertex);

        while(!potentiallyCloser.isEmpty()) {
            Vertex toConsider = potentiallyCloser.poll();
            //System.out.println(point.distanceSquared(toConsider.getPoint()));
            if(minDist > point.distanceTo(toConsider.getPoint())){
                closeVertex = toConsider;
                minDist = point.distanceTo(toConsider.getPoint());
                for (DirectedEdge edge : toConsider.getIncidentEdges()) {
                    potentiallyCloser.add(edge.getEnd());
                }
            }
        }
        return closeVertex;
    }
}
