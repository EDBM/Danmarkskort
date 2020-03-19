package BFST20Project;

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
    Map<Long, OSMRelation> idToRelation = new HashMap<>();
    private XMLStreamReader reader;
    private float minLat, minLon, maxLat, maxLon;

    public OSMParser(File file) throws FileNotFoundException, XMLStreamException {
        readOSMFile(file);
        createDrawables();

       /* for(long id : idToWay.keySet()){
            idToWay.get(id).printWay();
        }*/
    }

    private void readOSMFile(File file) throws XMLStreamException, FileNotFoundException {
        reader = XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file));

        while(reader.hasNext()){
            reader.next();

            if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                switch (reader.getLocalName()) {
                    case "bounds":
                        parseBounds();
                        break;
                    case "node":
                        parseNode();
                        break;
                    case "way":
                        parseWay();
                        break;
                    case "relation":
                        parseRelation();
                        break;
                }
            }
        }
    }

    private void parseBounds(){
        minLat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
        maxLon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
        maxLat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
        minLon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));
    }

    private void parseNode() throws XMLStreamException {
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

    private void parseWay() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        OSMWay osmWay = new OSMWay(id);

        readLoop: while(reader.hasNext()) {
            switch (reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
                    switch (reader.getLocalName()) {
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

    private void parseRelation() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        OSMRelation osmRelation = new OSMRelation(id);

        readLoop: while(reader.hasNext()){
            switch (reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
                    switch (reader.getLocalName()){
                        case "member":
                            if(reader.getAttributeValue(null, "type").equals("way")){
                                OSMWay referencedWay = idToWay.get(Long.parseLong(reader.getAttributeValue(null, "ref")));
                                osmRelation.addWay(referencedWay);
                            }
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if(reader.getLocalName().equals("relation"))
                        break readLoop;
                    break;
            }
            reader.next();
        }
        idToRelation.put(id, osmRelation);
    }

    private void createDrawables(){
        for(OSMWay way : idToWay.values()){
            Polylines line = new Polylines(way);
            drawables.add(line);
        }
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }

    public Iterator<Drawable> iterator() {
        return drawables.iterator();
    }

    public float getMinLat() {
        return minLat;
    }

    public float getMinLon() {
        return minLon;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public float getMaxLon() {
        return maxLon;
    }
}
