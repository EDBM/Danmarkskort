package BFST20Project.Routeplanner;

import BFST20Project.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

//TODO Some tests fail because of outdated distance calculations ( see testStraightLine() )
class ShortestPathTest {
    @Test
    public void testStraightLine(){
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(new Point(0,0), 0));
        vertices.add(new Vertex(new Point(1,1), 1));
        vertices.add(new Vertex(new Point(2,2), 2));
        vertices.add(new Vertex(new Point(3,3), 3));

        List<DirectedEdge> edges = new ArrayList<>();
        edges.add(new DirectedEdge(vertices.get(0), vertices.get(1), 1.0, "navn1", true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(2), 2.0, "navn2", true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(3), 3.0, "navn3", true, true));

        DirectedGraph graph = new DirectedGraph(vertices, edges);

        ShortestPath shortestPath = new ShortestPath(graph, 0, 3, false);

        Iterator<DirectedEdge> iterator = shortestPath.getPath().iterator();


        Assertions.assertEquals(0, iterator.next().getStart().getId());
        Assertions.assertEquals(1, iterator.next().getStart().getId());
        Assertions.assertEquals(2, iterator.next().getStart().getId());
        Assertions.assertFalse(iterator.hasNext());

        double dis1 = Point.distanceBetweenPoint(new Point(0, 0), new Point(1,1));
        double dis2 = Point.distanceBetweenPoint(new Point(1, 1), new Point(2,2));
        double dis3 = Point.distanceBetweenPoint(new Point(2, 2), new Point(3,3));
        Assertions.assertEquals(dis1+dis2+dis3, shortestPath.getTotalWeight());
    }

    @Test
    public void testBadDivergentPath(){
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(new Point(0,0), 0));
        vertices.add(new Vertex(new Point(1,1), 1));
        vertices.add(new Vertex(new Point(2,2), 2));
        vertices.add(new Vertex(new Point(3,3), 3));
        vertices.add(new Vertex(new Point(4,4), 4));

        List<DirectedEdge> edges = new ArrayList<>();
        edges.add(new DirectedEdge(vertices.get(0), vertices.get(1), 1.0, "navn1",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(2), 1.5, "navn2",true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(4), 1.5, "navn3",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(3), 1.0, "navn4",true, true));
        edges.add(new DirectedEdge(vertices.get(3), vertices.get(2), 1.0, "navn5",true, true));

        DirectedGraph graph = new DirectedGraph(vertices, edges);

        ShortestPath shortestPath = new ShortestPath(graph, 0, 4, true);

        Deque<DirectedEdge> path = shortestPath.getPath();

        Assertions.assertEquals(3, path.size());
        Assertions.assertEquals(4.0, shortestPath.getTotalWeight());
    }

    @Test
    public void testGoodDivergentPath(){
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(new Point(0,0), 0));
        vertices.add(new Vertex(new Point(1,1), 1));
        vertices.add(new Vertex(new Point(2,2), 2));
        vertices.add(new Vertex(new Point(3,3), 3));
        vertices.add(new Vertex(new Point(4,4), 4));

        List<DirectedEdge> edges = new ArrayList<>();
        edges.add(new DirectedEdge(vertices.get(0), vertices.get(1), 1.0, "navn1",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(2), 3.5, "navn2",true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(4), 2.0, "navn3",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(3), 1.5, "navn4",true, true));
        edges.add(new DirectedEdge(vertices.get(3), vertices.get(2), 1.0, "navn5",true, true));

        DirectedGraph graph = new DirectedGraph(vertices, edges);

        ShortestPath shortestPath = new ShortestPath(graph, 0, 4, true);

        Deque<DirectedEdge> path = shortestPath.getPath();

        Assertions.assertEquals(4, path.size());
        Assertions.assertEquals(5.5, shortestPath.getTotalWeight());
    }

    @Test
    public void testReversePath(){
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(new Point(0,0), 0));
        vertices.add(new Vertex(new Point(1,1), 1));
        vertices.add(new Vertex(new Point(2,2), 2));
        vertices.add(new Vertex(new Point(3,3), 3));
        vertices.add(new Vertex(new Point(4,4), 4));

        List<DirectedEdge> edges = new ArrayList<>();
        edges.add(new DirectedEdge(vertices.get(0), vertices.get(1), 1.0, "navn1",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(2), 3.5, "navn2",true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(4), 2.0, "navn3",true, true));
        edges.add(new DirectedEdge(vertices.get(1), vertices.get(3), 1.5, "navn4",true, true));
        edges.add(new DirectedEdge(vertices.get(3), vertices.get(2), 1.0, "navn5",true, true));

        edges.add(new DirectedEdge(vertices.get(1), vertices.get(0), 1.0, "navn1",true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(1), 3.5, "navn2",true, true));
        edges.add(new DirectedEdge(vertices.get(4), vertices.get(2), 2.0, "navn3",true, true));
        edges.add(new DirectedEdge(vertices.get(3), vertices.get(1), 1.5, "navn4",true, true));
        edges.add(new DirectedEdge(vertices.get(2), vertices.get(3), 1.0, "navn5",true, true));

        DirectedGraph graph = new DirectedGraph(vertices, edges);

        ShortestPath shortestPath = new ShortestPath(graph, 4, 0, true);

        Deque<DirectedEdge> path = shortestPath.getPath();

        Assertions.assertEquals(4, path.size());
        Assertions.assertEquals(5.5, shortestPath.getTotalWeight());
    }
}