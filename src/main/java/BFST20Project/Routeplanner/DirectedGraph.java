package BFST20Project.Routeplanner;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph {
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
}
