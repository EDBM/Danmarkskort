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

        if(previousEdge.getName()!=null) {
            for (DirectedEdge edge : path) {
                if (roadName.equals(edge.getName())) {
                    length += edge.getLength();
                    previousEdge = edge;
                } else if (!roadName.equals("")) {
                    //angle = calculateTurnAngle(previousEdge, edge);
                    angle = ((previousEdge.getEnd().getLon()-previousEdge.getStart().getLon())*(edge.getEnd().getLat()-previousEdge.getStart().getLat()))-
                            ((previousEdge.getEnd().getLat()-previousEdge.getStart().getLat())*(edge.getEnd().getLon()-previousEdge.getStart().getLon()));
                    if (angle > 0) {
                        direction = " drej til højre";
                    } else if (angle < 0) {
                        direction = " drej til venstre";
                    } else {
                        direction = " fortsæt ligeud";
                    }

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
        double bx1 = b.getStart().getLon();
        double by1 = b.getStart().getLat();
        double bx2 = b.getEnd().getLon();
        double by2 = b.getEnd().getLat();


        double aLength = Math.sqrt(Math.pow(Math.abs(ax1-bx1),2)+Math.pow(ay1-by1,2)); //length from point1 to point2
        double bLength = Math.sqrt(Math.pow(Math.abs(ax2-bx2),2)+Math.pow(ay2-by2,2)); // length from point2 to point3
        double cLength = Math.sqrt(Math.pow(Math.abs(ax1-bx2),2)+Math.pow(ay1-by2,2)); //length from point1 to point3

        //double aLength = a.getLength();
        //double bLength = b.getLength();
        //double cLength = Point.distanceBetweenPoint(a.getStart().getPoint(),(b.getEnd().getPoint()));

        double ab = (aLength*aLength+bLength*bLength);
        double cc = cLength*cLength;
        /*
        double twoab = 2*aLength*bLength;
        double abcc = ab-cc;
        double turnAngle = Math.acos(abcc/(twoab))*180/Math.PI;
*/
        double vectorABx = ax1 - bx1;
        double vectorABy = ay1 - by1;
        double vectorACx = ax1 - bx2;
        double vectorACy = ay1 - by2;
        double vectorBAx = bx1-ax1;

        double vectorBAy = by1 - ay1;
        double vectorBCx = bx2-bx1;
        double vectorBCy = by2-by1;
        double turnAngle = Math.acos((vectorABx*vectorACx+vectorABy*vectorACy)/(aLength*cLength));
        //double turnAngle = Math.acos((Math.pow(aLength,2)+Math.pow(cLength,2)-Math.pow(bLength,2))/(2*aLength*cLength));
        //turnAngle = turnAngle;


        return turnAngle;
    }
}
