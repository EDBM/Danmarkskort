package BFST20Project;

import java.util.ArrayList;
import java.util.List;

public class OSMWay {
    private long id;
    private List<OSMNode> nodes = new ArrayList<>();

    public OSMWay(long id) {
        this.id = id;
    }

    public void addNode(OSMNode node) {
        nodes.add(node);
    }

    public void printWay(){
        System.out.println(" id = " + id + " has " + nodes.size() + " nodes:");
        for(OSMNode node : nodes){
            System.out.print("    " + node.toString());
        }
    }
}
