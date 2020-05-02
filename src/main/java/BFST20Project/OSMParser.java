package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.TemporaryGraph;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

public class OSMParser extends Parser implements Serializable{
    Map<Long, OSMNode> idToNode = new TreeMap<>();
    Map<Long, OSMWay> idToWay = new HashMap<>();
    Map<Long, OSMRelation> idToRelation = new HashMap<>();
    List<OSMWay> traversableWays = new ArrayList<>();
    DirectedGraph drivableWaysGraph;
    private XMLStreamReader reader;
    public Trie trie = new Trie();

    private float minLat, minLon, maxLat, maxLon;
    private Bounds bounds;
    private EnumMap<ZoomLevel, KDTree> drawables = new EnumMap<>(ZoomLevel.class);
    private List<Drawable> islands = new ArrayList<>();

    public OSMParser(File file) throws IOException, XMLStreamException {
        System.out.println("load parser");
        readOSMFile(file);
        createDrivableWayGraph();
        createDrawables();
        //createBinaryFile();
    }

    private void readOSMFile(File file) throws XMLStreamException, FileNotFoundException {
        reader = XMLInputFactory.newFactory().createXMLStreamReader(new FileInputStream(file), "UTF8");

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

        //bounds = new Bounds(minLat, minLon, maxLat, maxLon);
    }

    private void parseNode() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        float lat = Float.parseFloat(reader.getAttributeValue(null,"lat"));
        float lon = Float.parseFloat(reader.getAttributeValue(null,"lon"));
        String[] address = new String[4]; //We could have used a Map, which would have been more intuitive and improve readability, but array was more cost effective

        while(reader.hasNext()) {
            switch (reader.getEventType()){
                case (XMLStreamConstants.START_ELEMENT):
                    switch (reader.getLocalName()){
                        case("tag"):
                            String key = reader.getAttributeValue(null,"k");
                            String value = reader.getAttributeValue(null,"v");
                            if (key.startsWith("addr:")){
                                String[] keyArray = key.split(":");
                                switch (keyArray[1]){
                                    case("street"):
                                        address[0]=value;
                                        break;
                                    case("housenumber"):
                                        address[1]=value;
                                        break;
                                    case("postcode"):
                                        address[2]=value;
                                        break;
                                    case("city"):
                                        address[3] = value;
                                        break;
                                }
                            }
                    }
            }
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("node"))
                break;
            reader.next();
        }
        OSMNode osmNode = new OSMNode(id, lat, lon);
        idToNode.put(id, osmNode);
        if(!Arrays.asList(address).contains(null)) {
            AddressParser a = new AddressParser(address[0], address[1], address[2], address[3]);
            trie.insert(a, Point.fromLonLat(lon, lat));
        }
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
                            if(key.startsWith("name")){
                                osmWay.setName(value);
                            }
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
        if(osmWay.isTraversableWay()){
            traversableWays.add(osmWay);
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
                                ((MultiPolygon) osmRelation).RingAssignment(); //TODO should be moved to the constructor
                                ((MultiPolygon) osmRelation).setMinMax();

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
        Map<ZoomLevel, List<Drawable>> tempDrawableStorage = new EnumMap<>(ZoomLevel.class);

        for(ZoomLevel zoomLevel : ZoomLevel.values()){
            tempDrawableStorage.put(zoomLevel, new ArrayList<>());
        }

        for(OSMWay way : idToWay.values()){
            Polylines line = new Polylines(way);
            rememberDrawable(line, tempDrawableStorage);
        }

        for(OSMRelation relation : idToRelation.values()){
            if(relation instanceof MultiPolygon)
                rememberDrawable((MultiPolygon) relation, tempDrawableStorage);
        }

        for(ZoomLevel zoomLevel : ZoomLevel.values()) {
            drawables.put(zoomLevel, new KDTree(tempDrawableStorage.get(zoomLevel)));
        }
    }

    private void rememberDrawable(Drawable drawable, Map<ZoomLevel, List<Drawable>> tempDrawableStorage) {
        if(drawable.getWayType() == WayType.ISLAND){
            islands.add(drawable);
        }
        else if(drawable.getWayType() != WayType.BEACH) {
            ZoomLevel zoomLevel = ZoomLevel.levelForWayType(drawable.getWayType());
            tempDrawableStorage.get(zoomLevel).add(drawable);
        }
    }

    private void createDrivableWayGraph() {
        TemporaryGraph temporaryGraph = new TemporaryGraph(traversableWays);
        temporaryGraph.createTemporaryGraph();
        drivableWaysGraph = temporaryGraph.compressedGraph();//TODO maybe rename this to something more fitting/precise.

        //int id1 = temporaryGraph.OSMIdToVertexId.get(32948578L);
        //int id2 = temporaryGraph.OSMIdToVertexId.get(4791600016L);

        //ShortestPath shortestPath = new ShortestPath(drivableWaysGraph, id1, id2, false);

        //System.out.println(shortestPath.getPath());
        //System.out.println("Total weight: " + shortestPath.getTotalWeight());
        //System.out.println(id1 + " " + id2);
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

    public Trie getTrie(){
        return trie;
    }



    public void createBinaryFile() throws IOException {
        System.out.println("binary parsing");
        File file = new File("C:\\Users\\Lucas\\IdeaProjects\\BFST20Gruppe8\\src\\main\\resources\\test.bin");

        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

        System.out.println("ways");
        int count = 0;
        for(OSMWay way : idToWay.values()){
            objectOut.writeObject(way);
            count++;
            System.out.println(count);
        }

        System.out.println("relations");
        for(OSMRelation relation : idToRelation.values()){
            objectOut.writeObject((MultiPolygon) relation);
        }

        System.out.println("bounds");
        //objectOut.writeObject(bounds);

        System.out.println("graph");
        objectOut.writeObject(new TemporaryGraph(traversableWays));

        System.out.println("islands");
        objectOut.writeObject(islands);

        System.out.println("trie");
        objectOut.writeObject(trie);

        objectOut.close();

    }


    private class Bounds implements Serializable{
        private Float minLat, minLon, maxLat, maxLon;

        public Bounds(float minLat, float minLon, float maxLat, float maxLon){
            this.minLat = minLat;
            this.minLon = minLon;
            this.maxLat = maxLat;
            this.maxLon = maxLon;
        }
    }



}
