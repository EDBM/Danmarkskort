package BFST20Project;

import org.checkerframework.checker.units.qual.A;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class OSMParser extends Parser implements Iterable<Drawable>{
    private List<Drawable> drawables = new ArrayList<>();
    Map<Long, OSMNode> idToNode = new TreeMap<>();
    Map<Long, OSMWay> idToWay = new HashMap<>();
    private XMLStreamReader reader;
    float minlat, minlon, maxlat, maxlon;

    public OSMParser(File file) throws FileNotFoundException, XMLStreamException {
        reader = XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file));

        while(reader.hasNext()){
            reader.next();

            if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                switch (reader.getLocalName()) {
                    case "node":
                        parseNode();
                        break;
                    case "way":
                        parseWay();
                        break;
                }
            }
        }

       /* for(long id : idToWay.keySet()){
            idToWay.get(id).printWay();
        }*/
    }

    public void parseNode() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        float lat = Float.parseFloat(reader.getAttributeValue(null,"lat"));
        float lon = Float.parseFloat(reader.getAttributeValue(null,"lon"));

        while(reader.hasNext()) {
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("node"))
                break;
            reader.next();
        }
        var osmNode = new OSMNode(id, lat, lon);
        idToNode.put(id, osmNode);
    }

    public void parseWay() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        OSMWay osmWay = new OSMWay();

        readLoop: while(reader.hasNext()) {
            switch (reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "bounds":
                            minlat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
                            maxlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
                            maxlat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
                            minlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));
                            break;
                        case "nd":
                            osmWay.addNode(idToNode.get(Long.parseLong(reader.getAttributeValue(null, "ref"))));
                            break;
                        case "tag":
                            var key = reader.getAttributeValue(null, "k");
                            var value = reader.getAttributeValue(null, "v");
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if(reader.getLocalName().equals("way"))
                        break readLoop;
                    break;
            }
            reader.next();
        }

        idToWay.put(id, osmWay);
    }
    public Iterator<Drawable> iterator() {
        return drawables.iterator();
    }
}
