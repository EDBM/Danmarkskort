package BFST20Project;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OSMParser extends Parser {
    Map<Long, OSMNode> idToNode = new TreeMap<>();
    Map<Long, OSMWay> idToWay = new HashMap<>();
    private XMLStreamReader reader;


    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        OSMParser parser = new OSMParser();
        parser.read(new File("C:\\Users\\johan\\repositories\\BFST20Project\\src\\main\\java\\BFST20Project\\map.osm"));
    }

    public void read(File file) throws FileNotFoundException, XMLStreamException {
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

        for(long id : idToWay.keySet()){
            idToWay.get(id).printWay();
        }
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
}
