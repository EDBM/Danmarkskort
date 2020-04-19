package BFST20Project.Routeplanner;

import BFST20Project.OSMNode;
import BFST20Project.OSMWay;
import BFST20Project.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryGraph {
    public Map<Long, Integer> OSMIdToVertexId = new HashMap<>();
    List<Vertex> vertices = new ArrayList<>();
    List<DirectedEdge> edges = new ArrayList<>();

    public TemporaryGraph(List<OSMWay> drivableWays) {
        for (OSMWay way: drivableWays ) {
            for (OSMNode node : way.getAll()){
                createVertex(node);
            }
        }

        for(OSMWay way : drivableWays){
            createEdges(way);
        }
    }

    private void createVertex(OSMNode node) {
        if(!OSMIdToVertexId.containsKey(node.getId())){
            int n = vertices.size();
            vertices.add(new Vertex(Point.fromLonLat(node.getLon(), node.getLat()), n));
            OSMIdToVertexId.put(node.getId(), n);
        }
    }

    private void createEdges(OSMWay way) {
        List<OSMNode> nodes = way.getAll();
        for(int i = 0; i < nodes.size() - 1; i++){
            Vertex v = vertices.get(OSMIdToVertexId.get(nodes.get(i).getId()));
            Vertex w = vertices.get(OSMIdToVertexId.get(nodes.get(i+1).getId()));

            edges.add(new DirectedEdge(v, w, way.getSpeed()));
            edges.add(new DirectedEdge(w, v, way.getSpeed()));
        }
    }

    public DirectedGraph compressedGraph(){
        return new DirectedGraph(vertices, edges);
    }
}
