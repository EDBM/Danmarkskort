package BFST20Project;

import BFST20Project.Routeplanner.DirectedEdge;
import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.Vertex;

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

    private Vertex navigateFrom, navigateTo;
    private Polylines shortestPath;

    private Trie trie;

    public Model() throws FileNotFoundException, XMLStreamException {
        file = new File(getClass().getClassLoader().getResource("bornholm.osm").getFile());


        OSMParser osmParser = new OSMParser(file);
        drawables = osmParser.getDrawables();
        islands = osmParser.getIslands();
        driveableWayGraph = osmParser.getDrivableWayGraph();
        minLat = osmParser.getMinLat();
        minLon = osmParser.getMinLon();
        maxLat = osmParser.getMaxLat();
        maxLon = osmParser.getMaxLon();

        trie = osmParser.trie;

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

    public Trie getTrie() {
        return trie;
    }

    public Drawable nearestRoad(Point mapPoint) {
        Collection<WayType> allowedWaytypes = Arrays.asList(WayType.MOTORWAY, WayType.HIGHWAY, WayType.SECONDARY, WayType.DIRTROAD, WayType.MINIWAY);
        List<Drawable> closestRoads = new ArrayList<>();

        for (KDTree kdTree : drawables.values()){
            closestRoads.add(kdTree.nearestNeighborOfTypes(mapPoint, allowedWaytypes));
        }

        Drawable closest = closestRoads.get(0);

        for(int i = 1; i < closestRoads.size(); i++){
            if(closestRoads.get(i) != null) {
                if (closest == null || closestRoads.get(i).distanceTo(mapPoint) < closest.distanceTo(mapPoint))
                    closest = closestRoads.get(i);
            }
        }

        return closest;
    }

    private void navigate(){
        if(navigateFrom != null && navigateTo != null){
            Deque<DirectedEdge> edges = new ShortestPath(driveableWayGraph, navigateFrom.getId(), navigateTo.getId()).getPath();
            Point[] points = new Point[edges.size() + 1];
            points[0] = edges.getFirst().getStart().getPoint();

            int n = 1;
            for(DirectedEdge edge : edges){
                points[n] = edge.getEnd().getPoint();
                n++;
            }
            shortestPath = new Polylines(points, WayType.SHORTEST_PATH);

            notifyObservers();
        }
    }

    public void setNavigateFrom(Point from) {
        Drawable nearestRoad = nearestRoad(from);
        if(nearestRoad.getVertex() != null) {
            navigateFrom = nearestRoad.getVertex();
            navigate();
        }
    }

    public void setNavigateTo(Point to) {
        Drawable nearestRoad = nearestRoad(to);
        if(nearestRoad.getVertex() != null) {
            navigateTo = nearestRoad.getVertex();
            navigate();
        }
    }
}
