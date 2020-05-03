package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.TemporaryGraph;
import BFST20Project.Trie.Trie;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

public class OSMParser extends Parser implements Serializable{
    private Map<Long, OSMNode> idToNode = new TreeMap<>();
    private Map<Long, OSMWay> idToWay = new HashMap<>();
    private Map<Long, OSMRelation> idToRelation = new HashMap<>();
    private List<OSMWay> traversableWays = new ArrayList<>();
    private DirectedGraph drivableWaysGraph;
    private XMLStreamReader reader;
    private Trie trie = new Trie();

    private float minLat, minLon, maxLat, maxLon;
    private EnumMap<ZoomLevel, KDTree> drawables = new EnumMap<>(ZoomLevel.class);


    public OSMParser(InputStream inputStream) throws XMLStreamException {
        System.out.println("load parser");
        readOSMFile(inputStream);
        //createBinaryFile();
        createDrivableWayGraph();
        createDrawables();
    }

    private void readOSMFile(InputStream inputStream) throws XMLStreamException {
        reader = XMLInputFactory.newFactory().createXMLStreamReader(inputStream, "UTF8");

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
        String[] addressParts = new String[4]; //We could have used a Map, which would have been more intuitive and improve readability, but array was more cost effective

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
                                        addressParts[0]=value;
                                        break;
                                    case("housenumber"):
                                        addressParts[1]=value;
                                        break;
                                    case("postcode"):
                                        addressParts[2]=value;
                                        break;
                                    case("city"):
                                        addressParts[3] = value;
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
        if(!Arrays.asList(addressParts).contains(null)) {
            Address address = new Address(addressParts[0], addressParts[1], addressParts[2], addressParts[3]);
            trie.insert(address, Point.fromLonLat(lon, lat));
        }
    }

    private void parseWay() throws XMLStreamException {
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        OSMWay osmWay = new OSMWay();

        Map<String, String> tags = new HashMap<>();

        readLoop: while(reader.hasNext()){
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

    private void parseRelation() throws XMLStreamException{
        long id = Long.parseLong(reader.getAttributeValue(null, "id"));
        List<OSMWay> OSMWays = new ArrayList<>();
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
                            }
                            break;
                        case "tag":
                            String key = reader.getAttributeValue(null, "k");
                            String value = reader.getAttributeValue(null, "v");
                            if(value.equals("multipolygon")){
                                osmRelation = new MultiPolygon(id);
                                osmRelation.addAllWays(OSMWays);
                                ((MultiPolygon) osmRelation).RingAssignment();
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
                    }
                    break;
            }

            reader.next();
        }

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



    private void createDrivableWayGraph() {
        TemporaryGraph temporaryGraph = new TemporaryGraph(traversableWays);
        temporaryGraph.createTemporaryGraph();
        drivableWaysGraph = temporaryGraph.compressedGraph();
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

        System.out.println("graph");
        TemporaryGraph temporaryGraph = new TemporaryGraph(traversableWays);
        objectOut.writeObject(temporaryGraph);

        System.out.println("ways");
        for(OSMWay way : idToWay.values()){
            objectOut.writeObject(way);
        }

        System.out.println("relations");
        for(OSMRelation relation : idToRelation.values()){
            objectOut.writeObject((MultiPolygon) relation);
        }

        System.out.println("bounds");
        //objectOut.writeObject(bounds);


        System.out.println("islands");
        objectOut.writeObject(islands);

        System.out.println("trie");
        objectOut.writeObject(trie);

        objectOut.close();

    }
}



