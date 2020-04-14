package BFST20Project.Routeplanner;

import java.util.*;

public class RoutePlanner {
        private Graph graph;
        RouteNode startNode;
        RouteNode endNode;
        IndexMinPQ<Double> nodesToCheck;
        ArrayList<RouteNode> completedNodes;
        Map<RouteNode,RouteNode> visitedEdges;
        Map<RouteNode, Double> distTo;

    public RoutePlanner(RouteNode startNode, RouteNode endNode){
        this.startNode=startNode;
        this.endNode=endNode;
        nodesToCheck = new PriorityQueue<>();
        completedNodes = new ArrayList<>();
        visitedEdges = new HashMap<>();
        distTo = new HashMap<>();
        }

    public List<Edge> findRoute(RouteNode startNode, RouteNode endNode) {
        distTo.put(startNode, 0.0);
        nodesToCheck.add(new WeightedNode(startNode, 0.0));

        while(!nodesToCheck.isEmpty()){
            WeightedNode nodeToRelax = nodesToCheck.poll();
            if(nodeToRelax.node == endNode){
                break;
            }
            relax(nodeToRelax.node);
        }

    }

    public void relax(RouteNode node){
        for(Edge edge: node.incidentEdges){
            RouteNode n = edge.getEndNode();
            if(distTo.get(n)>distTo.get(node)+edge.getLength()){
                distTo.put(n, distTo.get(node)+edge.getLength());
                node.addEdge(edge);
                WeightedNode weightedNode = new WeightedNode(node, distTo.get(node)+edge.getLength());
                nodesToCheck.add(weightedNode);
            }
        }
    }

    private class WeightedNode implements Comparable<WeightedNode>{
        private RouteNode node;
        private Double weight;

        private WeightedNode(RouteNode node, Double weight){
            this.node = node;
            this.weight = weight;
        }

        @Override
        public int compareTo(WeightedNode other) {
            return weight.compareTo(other.weight);
        }
    }
}
