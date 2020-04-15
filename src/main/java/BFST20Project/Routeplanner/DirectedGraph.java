package BFST20Project.Routeplanner;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph {
    private final List<Vertex> vertices;
    private final List<DirectedEdge> edges;

    public DirectedGraph(List<Vertex> vertices, List<DirectedEdge> edges){
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
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
