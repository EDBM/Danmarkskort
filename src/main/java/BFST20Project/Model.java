package BFST20Project;

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

    public Model() throws FileNotFoundException, XMLStreamException {
        file = new File(getClass().getClassLoader().getResource("bornholm.osm").getFile());


        OSMParser osmParser = new OSMParser(file);
        drawables = osmParser.getDrawables();
        islands = osmParser.getIslands();
        minLat = osmParser.getMinLat();
        minLon = osmParser.getMinLon();
        maxLat = osmParser.getMaxLat();
        maxLon = osmParser.getMaxLon();
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
        for(ZoomLevel zoomLevel : ZoomLevel.values()){
            if(desiredZoomLevel.compareTo(zoomLevel) >= 0){
                toDraw.putAll(drawables.get(zoomLevel).query(min.getX(), min.getY(), max.getX(), max.getY()));
            }
        }
        return toDraw;
    }
}
