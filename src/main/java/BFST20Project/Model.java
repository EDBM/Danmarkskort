package BFST20Project;

import BFST20Project.Routeplanner.DirectedEdge;
import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.Vertex;
import javafx.scene.control.TextField;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Model implements Serializable {
    public float minLat,minLon, maxLat, maxLon;
    List<Runnable> observers = new ArrayList<>();
    File file;
    private List<Drawable> islands;
    private EnumMap<ZoomLevel, KDTree> drawables;
    private DirectedGraph driveableWayGraph;

    private Vertex navigateFrom, navigateTo;
    private Polylines shortestPath;
    public Boolean routeIsChanged;
    public String routeAsText;

    private Trie trie;

    private Boolean isDriving = true;

    public Model(File file) throws IOException, XMLStreamException, ClassNotFoundException {
        loadModel(file);
    }

    public void loadModel(File file) throws IOException, XMLStreamException, ClassNotFoundException {

        file = new File(getClass().getClassLoader().getResource("bornholm.zip").getFile());
        String extension = getFileExtension(file.toString());
        Parser parser;

        if(extension.equalsIgnoreCase("zip")){
            parser = readZipFile(file);

        }
        else if(extension.equalsIgnoreCase("osm")){
            parser = readOSMStream(new FileInputStream(file));
        }
        else if(extension.equalsIgnoreCase("bin")){
            parser = readBinStream(new FileInputStream(file));
        }
        else{
            throw new IOException("File is bad");
        }

        /*if(file == null) {
            System.out.println("binary parser");
            parser = new BinaryParser(new File("C:\\Users\\Lucas\\IdeaProjects\\BFST20Gruppe8\\src\\main\\resources\\test.bin"));
        } else {
            System.out.println("OSM parser");
            parser = new OSMParser(file);
        }*/



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

    private Parser readZipFile(File file) throws IOException, XMLStreamException, ClassNotFoundException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()){
            ZipEntry zipEntry = entries.nextElement();
            String extension = getFileExtension(zipEntry.toString());
            if(extension.equalsIgnoreCase("osm")){
                return readOSMStream(zipFile.getInputStream(zipEntry));
            }
            else if(extension.equalsIgnoreCase("bin")){
                return readBinStream(zipFile.getInputStream(zipEntry));
            }
        }
        throw new IOException("Zip-file did not contain osm or bin files");
    }

    private Parser readBinStream(InputStream inputStream) throws IOException, ClassNotFoundException {
        return new BinaryParser(inputStream);
    }

    private Parser readOSMStream(InputStream inputStream) throws IOException, XMLStreamException {
        return new OSMParser(inputStream);
    }

    private String getFileExtension(String file) {
        if(file.contains(".")){
            return  file.substring(file.lastIndexOf(".") + 1);
        }
        return "";
    }


    public void addObserver(Runnable observer) {
        if (observers != null) observers.add(observer);
    }

    public void notifyObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
        routeIsChanged = false;
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
        System.out.println("navigate");
        if(navigateFrom != null && navigateTo != null){
            ShortestPath sp = new ShortestPath(driveableWayGraph, navigateFrom.getId(), navigateTo.getId(), isDriving);
            Deque<DirectedEdge> edges = sp.getPath();
            Point[] points = new Point[edges.size() + 1];
            points[0] = edges.getFirst().getStart().getPoint();

            int n = 1;
            for(DirectedEdge edge : edges){
                points[n] = edge.getEnd().getPoint();
                n++;
            }
            shortestPath = new Polylines(points, WayType.SHORTEST_PATH);
            routeAsText = String.join("\n", sp.textRoutePlanner()).replace(", ","\n");
            routeIsChanged = true;
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

    public void navigateFromAddress(String address) {
        Point navigateFrom = trie.searchTrie(address);

        if(navigateFrom != null){
            setNavigateFrom(navigateFrom);
        }
    }

    public void navigateToAddress(String address){
        Point navigateTo = trie.searchTrie(address);

        if(navigateTo != null){
            setNavigateTo(navigateTo);
        }
    }

    public void setDriving(Boolean driving) {
        isDriving = driving;
        navigate();
    }
}
