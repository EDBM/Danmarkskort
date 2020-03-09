package BFST20Project;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public float minLat,minLon, maxLat, maxLon;
    List<Runnable> observers = new ArrayList<>();
    File file = new File("C:\\Users\\Andreas\\IdeaProjects\\BFST20Gruppe8\\src\\main\\Data\\Samsoe.osm");
    OSMParser osmParser = new OSMParser(file);
    private List<Drawable> drawables = new ArrayList<>();

    public Model() throws FileNotFoundException, XMLStreamException {
        observers = new ArrayList<>();
    }

    public void addObserver(Runnable observer) {
        if (observers != null) observers.add(observer);
    }

    public void notifyObservers() {
        for (var observer : observers) {
            observer.run();
        }
    }
}
