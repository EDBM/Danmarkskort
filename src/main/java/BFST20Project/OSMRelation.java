package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

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

    public void addWay(OSMWay way){
        ways.add(way);
    }

    public void addAllWays(ArrayList<OSMWay> osmWays) { ways.addAll(osmWays); }

    public WayType getWayType() {
        return type;
    }

    public void setWayType(WayType type) {
        this.type = type;
    }

    public List<Point> getCoordinates() {

        return new ArrayList<>();
    }
}