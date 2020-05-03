package BFST20Project;

import BFST20Project.Routeplanner.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OSMWay implements Serializable {
    private WayType type;
    private String name;
    transient private Vertex vertex;
    private List<OSMNode> nodes = new ArrayList<>();

    public OSMNode first() {return nodes.get(0);}

    public OSMNode last() {return nodes.get(size()-1);}

    public void addNode(OSMNode node) {nodes.add(node);}

    public void setName(String name){this.name=name;}

    public String getName(){return name;}

    public OSMNode get(int index){ return nodes.get(index); }

    public int size(){return nodes.size();}

    public void setType(WayType type) {
        this.type = type;
    }

    public WayType getWayType() {
        return type;
    }

    public List<OSMNode> getAll(){ return nodes; }

    public int getSpeed(){
        if(type.equals(WayType.MOTORWAY)){ return 130; }
        if(type.equals(WayType.SECONDARY)){ return 80; }
        return 50;
    }

    public boolean isTraversableWay() {
        return (type.equals(WayType.HIGHWAY)
            || type.equals(WayType.MOTORWAY)
            || type.equals(WayType.SECONDARY)
            || type.equals(WayType.MINIWAY)
            || type.equals(WayType.DIRTROAD));
    }

    public boolean isDrivableWay() {
        return (type.equals(WayType.HIGHWAY)
                || type.equals(WayType.MOTORWAY) //Drivable only, walking not permitted
                || type.equals(WayType.SECONDARY)
                || type.equals(WayType.DIRTROAD));
    }

    public boolean isWalkableWay() {
        return (type.equals(WayType.HIGHWAY)
                || type.equals(WayType.SECONDARY)
                || type.equals(WayType.MINIWAY) //Walkable only, driving not permitted
                || type.equals(WayType.DIRTROAD));
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }
}