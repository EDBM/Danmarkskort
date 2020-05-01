package BFST20Project;

import BFST20Project.Routeplanner.DirectedEdge;
import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.Vertex;
import javafx.scene.control.TextField;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;


public class Model implements Serializable {
    public float minLat,minLon, maxLat, maxLon;
    List<Runnable> observers = new ArrayList<>();
    File file;
    private List<Drawable> islands;
    private EnumMap<ZoomLevel, KDTree> drawables;
    private DirectedGraph driveableWayGraph;

    private Vertex navigateFrom, navigateTo;
    private Polylines shortestPath;

    private Trie trie;

    public Model() throws IOException, XMLStreamException, ClassNotFoundException {
        file = new File(getClass().getClassLoader().getResource("bornholm.osm").getFile());
        //file = new File("F:\\denmark-latest.osm");

        OSMParser parser = new OSMParser(file);
        //BinaryParser parser = new BinaryParser(new File("C:\\Users\\Lucas\\IdeaProjects\\BFST20Gruppe8\\src\\main\\resources\\test.bin"));

        drawables = parser.getDrawables();
        drawables = parser.getDrawables();
        islands = parser.getIslands();
        driveableWayGraph = parser.getDrivableWayGraph();
        minLat = parser.getMinLat();
        minLon = parser.getMinLon();
        maxLat = parser.getMaxLat();
        maxLon = parser.getMaxLon();
        //TODO bounds not being Serialized properly
        minLat = -55.3041f;
        minLon = 8.218784f;
        maxLat = -54.9264f;
        maxLon = 8.491f;
        trie = parser.getTrie();
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

    //zoomLevel = 4, means we get the nearestRoad in all the zoomlevels
    public Drawable nearestRoad(Point mapPoint, Set<ZoomLevel> zoomLevel) {
        Collection<WayType> allowedWaytypes = Arrays.asList(WayType.MOTORWAY, WayType.HIGHWAY, WayType.SECONDARY, WayType.DIRTROAD, WayType.MINIWAY);
        List<Drawable> closestRoads = new ArrayList<>();

            for (KDTree kdTree : drawables.values()) {
                for (ZoomLevel ZoomLevel : zoomLevel) {
                    if(drawables.get(ZoomLevel) == kdTree){
                        closestRoads.add(kdTree.nearestNeighborOfTypes(mapPoint, allowedWaytypes));
                    }
                }
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
            Deque<DirectedEdge> edges = new ShortestPath(driveableWayGraph, navigateFrom.getId(), navigateTo.getId()).getPath(); //TODO is always driving
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
        Drawable nearestRoad = nearestRoad(from, drawables.keySet());
        if(nearestRoad.getVertex() != null) {
            navigateFrom = driveableWayGraph.nearestVertex(nearestRoad.getVertex(), from);
            navigate();
        }
    }

    public void setNavigateTo(Point to) {
        Drawable nearestRoad = nearestRoad(to, drawables.keySet());
        if(nearestRoad.getVertex() != null) {
            navigateTo = driveableWayGraph.nearestVertex(nearestRoad.getVertex(), to);
            navigate();
        }
    }
}
