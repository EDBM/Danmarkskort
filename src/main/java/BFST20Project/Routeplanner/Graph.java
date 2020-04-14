package BFST20Project.Routeplanner;

import java.util.List;

public class Graph {

    private final List<RouteNode> routeNodes;
    private final List<Edge> edges;

    public Graph(List<RouteNode> routeNodes,List<Edge> edges){
        this.routeNodes=routeNodes;
        this.edges=edges;
    }

    public List<RouteNode> getRouteNodes() {
        return routeNodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
