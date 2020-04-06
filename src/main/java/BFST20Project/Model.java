package BFST20Project;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Model {
    public float minLat,minLon, maxLat, maxLon;
    List<Runnable> observers = new ArrayList<>();
    File file;
    private EnumMap<WayType, List<Drawable>> drawables;

    public Model() throws FileNotFoundException, XMLStreamException {
        file = new File(getClass().getClassLoader().getResource("bornholm.osm").getFile());
        //file = new File("C:\\Users\\Lucas\\Downloads\\denmark-latest.osm");


        OSMParser osmParser = new OSMParser(file);
        drawables = osmParser.getDrawables();
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

    public EnumMap<WayType, List<Drawable>> getDrawables() {
        return drawables;
    }

}
