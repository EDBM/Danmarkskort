package BFST20Project;

import java.util.ArrayList;
import java.util.List;

public class OSMWay{
    private long id;
    private WayType type;
    private List<OSMNode> nodes = new ArrayList<>();

    public OSMWay(long id) {
        this.id = id;
    }

    public OSMNode first() {
        return nodes.get(0);
    }

    public OSMNode last() {
        return nodes.get(size()-1);
    }

    public void addNode(OSMNode node) {
        nodes.add(node);
    }

   /* public void printWay(){
        System.out.println(" id = " + id + " has " + nodes.size() + " nodes:");
        for(OSMNode node : nodes){
            System.out.print("    " + node.toString());
        }
    }*/
    public OSMNode get(int index){return nodes.get(index);}

    public int size(){return nodes.size();}

    public void setType(WayType type) {
        this.type = type;
    }

    public WayType getType() {
        return type;
    }
}
