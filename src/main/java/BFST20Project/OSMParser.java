package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.TemporaryGraph;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class OSMParser extends Parser{
    Map<Long, OSMNode> idToNode = new TreeMap<>();
    Map<Long, OSMWay> idToWay = new HashMap<>();
    Map<Long, OSMRelation> idToRelation = new HashMap<>();
    List<OSMWay> drivableWays = new ArrayList<>();
    DirectedGraph drivableWaysGraph;
    private XMLStreamReader reader;

    private float minLat, minLon, maxLat, maxLon;
    private EnumMap<ZoomLevel, KDTree> drawables = new EnumMap<>(ZoomLevel.class);
    private List<Drawable> islands = new ArrayList<>();

    public OSMParser(File file) throws FileNotFoundException, XMLStreamException {
        System.out.println("load parser");
        readOSMFile(file);
        createDrawables();
        createDrivableWayGraph();
    }

    private void readOSMFile(File file) throws XMLStreamException, FileNotFoundException {
        reader = XMLInputFactory.newFactory().createXMLStreamReader(new FileReader(file));

        int count = 0;

        while(reader.hasNext()){
            reader.next();

            if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                switch (reader.getLocalName()) {
                    case "bounds":
                        parseBounds();
                        break;
                    case "node":
                        parseNode();
                        count++;
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

        System.out.println("number of nodes on map: " + count);
        //calculateBounds();
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

        Map<String, String> tags = new HashMap<>();

        readLoop: while(reader.hasNext()) {
            switch (reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "nd":
                            long nodeId = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            osmWay.addNode(idToNode.get(nodeId));
                            break;
                        case "tag":
                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");
                            tags.put(key, value);
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

        osmWay.setType(WayTypeSetter.typeFromTags(tags));
        if(osmWay.isDrivableWay()){
            drivableWays.add(osmWay);
        }

        idToWay.put(id, osmWay);
    }

    private void parseRelation() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        ArrayList<OSMWay> OSMWays = new ArrayList<>();
        OSMRelation osmRelation = new OSMRelation(id);

        Map<String, String> tags = new HashMap<>();

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
                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");
                            if(value.equals("multipolygon")){
                                osmRelation = new MultiPolygon(id);
                                osmRelation.addAllWays(OSMWays);
                                ((MultiPolygon) osmRelation).RingAssignment();
//                                ((MultiPolygon) osmRelation).calculateBoundingBox();
                                idToRelation.put(id, osmRelation);
                            }
                            tags.put(key, value);
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


        //System.out.println("type from tags: " + WayTypeSetter.typeFromTags(tags) + " id: " + id);

        osmRelation.setWayType(WayTypeSetter.typeFromTags(tags));
    }

    private void createDrawables(){
        for(ZoomLevel zoomLevel : ZoomLevel.values())
            drawables.put(zoomLevel, new KDTree());

        for(OSMWay way : idToWay.values()){
            Polylines line = new Polylines(way);
            rememberDrawable(line);
        }

        for(OSMRelation relation : idToRelation.values()){
            rememberDrawable(relation);
        }

    }

    private void rememberDrawable(Drawable drawable) {
        if(drawable.getWayType() == WayType.ISLAND){
            islands.add(drawable);
        }
        else if(drawable.getWayType() != WayType.BEACH) {
            ZoomLevel zoomLevel = ZoomLevel.levelForWayType(drawable.getWayType());
            drawables.get(zoomLevel).insertDrawable(drawable);
        }
    }

    private void createDrivableWayGraph() {
        TemporaryGraph temporaryGraph = new TemporaryGraph(drivableWays);
        drivableWaysGraph = temporaryGraph.compressedGraph();

        int id1 = temporaryGraph.OSMIdToVertexId.get(32948578L);
        int id2 = temporaryGraph.OSMIdToVertexId.get(4791600016L);
/*
        ShortestPath shortestPath = new ShortestPath(drivableWaysGraph, id1, id2);

        System.out.println(shortestPath.getPath());
        System.out.println(shortestPath.getTotalWeight());*/
        System.out.println(id1 + " " + id2);
    }

    public EnumMap<ZoomLevel, KDTree> getDrawables() {
        return drawables;
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

    public List<Drawable> getIslands() {
        return islands;
    }

    public DirectedGraph getDrivableWayGraph() {
        return drivableWaysGraph;
    }
}
