package BFST20Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OSMRelation implements Serializable {
    long id;
    List<OSMWay> ways = new ArrayList<>();
    private WayType type;

    public OSMRelation(long id) {
        this.id = id;
    }

    public void addAllWays(List<OSMWay> osmWays) { ways.addAll(osmWays); }

    public WayType getWayType() {
        return type;
    }

    public void setWayType(WayType type) {
        this.type = type;
    }

}