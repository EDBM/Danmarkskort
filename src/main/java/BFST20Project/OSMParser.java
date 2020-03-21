package BFST20Project;

import javax.management.relation.Relation;
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
        calculateBounds();
    }

    //Obviously slow but makes debugging way easier as the view actually zooms correctly for OSM files without defined bounds
    private void calculateBounds() {
        float minLat = Float.MAX_VALUE, minLon = Float.MAX_VALUE;
        float maxLat = Float.MIN_VALUE, maxLon = Float.MIN_VALUE;
        for (OSMNode node: idToNode.values()) {
            if(node.getLat() > maxLat) maxLat = node.getLat();
            if(node.getLon() > maxLon) maxLon = node.getLon();

            if(node.getLat() < minLat) minLat = node.getLat();
            if(node.getLon() < minLon) minLon = node.getLon();
        }

        this.minLat = -minLat;
        this.minLon = 0.56f * minLon;
        this.maxLat = -maxLat;
        this.maxLon = 0.56f * maxLon;
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
        OSMNode osmNode = new OSMNode(id, lat, lon);
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
                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");
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
        ArrayList<OSMWay> OSMWays = new ArrayList<>();
        //OSMRelation osmRelation = new OSMRelation(id);

        readLoop: while(reader.hasNext()){
            switch (reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
                    switch (reader.getLocalName()){
                        case "member":
                            if(reader.getAttributeValue(null, "type").equals("way")){
                                OSMWay referencedWay = idToWay.get(Long.parseLong(reader.getAttributeValue(null, "ref")));
                                OSMWays.add(referencedWay);
                                //osmRelation.addWay(referencedWay);
                            }
                            break;
                        case "tag":
                            if(reader.getAttributeValue(null, "v").equals("multipolygon")){
                                MultiPolygon multiPolygon = new MultiPolygon(id);
                                multiPolygon.addAllWays(OSMWays);
                                multiPolygon.RingAssignment();
                                idToRelation.put(id, multiPolygon);
                            }
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if(reader.getLocalName().equals("relation")) {
                        break readLoop;
                    } else {
                        break;
                    }
            }
            reader.next();
        }
    }

    private void createDrawables(){
        for(OSMWay way : idToWay.values()){
            Polylines line = new Polylines(way);
            drawables.add(line);
        }
        for(OSMRelation relation : idToRelation.values()){
            drawables.add(relation);
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
