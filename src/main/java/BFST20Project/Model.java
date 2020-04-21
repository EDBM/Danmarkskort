package BFST20Project;

import BFST20Project.Routeplanner.DirectedEdge;
import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Model {
    public float minLat,minLon, maxLat, maxLon;
    List<Runnable> observers = new ArrayList<>();
    File file;
    private List<Drawable> islands;
    private EnumMap<ZoomLevel, KDTree> drawables;
    private DirectedGraph driveableWayGraph;
    private Polylines shortestPath;

    public Model() throws FileNotFoundException, XMLStreamException {
        file = new File(getClass().getClassLoader().getResource("anholt.osm").getFile());


        OSMParser osmParser = new OSMParser(file);
        drawables = osmParser.getDrawables();
        islands = osmParser.getIslands();
        driveableWayGraph = osmParser.getDrivableWayGraph();
        minLat = osmParser.getMinLat();
        minLon = osmParser.getMinLon();
        maxLat = osmParser.getMaxLat();
        maxLon = osmParser.getMaxLon();
/*
        Deque<DirectedEdge> edges = new ShortestPath(driveableWayGraph, 1732, 59569).getPath();
        Point[] points = new Point[edges.size() + 1];
        points[0] = edges.getFirst().getStart().getPoint();

        System.out.println(edges.size());

        int n = 1;
        for(DirectedEdge edge : edges){
            points[n] = edge.getEnd().getPoint();
            n++;
        }
        shortestPath = new Polylines(points, WayType.SHORTEST_PATH);*/
    }

    public void addObserver(Runnable observer) {
        if (observers != null) observers.add(observer);
    }

    public void notifyObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }

    public EnumMap<WayType, List<Drawable>> getDrawables(ZoomLevel desiredZoomLevel, Point min, Point max) {
        EnumMap<WayType, List<Drawable>> toDraw = new EnumMap<>(WayType.class);
        toDraw.put(WayType.ISLAND, islands);
        if(shortestPath != null) {
            toDraw.put(WayType.SHORTEST_PATH, (Arrays.asList(new Polylines[]{shortestPath})));
        }
        for(ZoomLevel zoomLevel : ZoomLevel.values()){
            if(desiredZoomLevel.compareTo(zoomLevel) >= 0){
                toDraw.putAll(drawables.get(zoomLevel).query(min.getX(), min.getY(), max.getX(), max.getY()));
            }
        }
        return toDraw;
    }
}
