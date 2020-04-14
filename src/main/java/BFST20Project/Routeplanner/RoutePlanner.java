package BFST20Project.Routeplanner;

import java.util.*;

public class RoutePlanner {
        private List<RouteNode> nodes;
        RouteNode startNode;
        RouteNode endNode;
        Queue<Edge> edges;
        Map<RouteNode,RouteNode> visitedEdges;
        Map<RouteNode, Double> distTo;

    public RoutePlanner(RouteNode startNode, RouteNode endNode){
        this.startNode=startNode;
        this.endNode=endNode;
        Queue<Edge> edges = new PriorityQueue<>();
        List<RouteNode> nodes = new ArrayList<>();
        visitedEdges = new HashMap<>();
        distTo = new HashMap<>();
        }

    public List<Edge> findRoute(RouteNode startNode, RouteNode endNode) {
        RouteNode currentNode = startNode;
        for(Edge edge: currentNode.incidentEdges){
            distTo.put(edge.getEndNode(),edge.getDistance());
        }
        
    }


}
