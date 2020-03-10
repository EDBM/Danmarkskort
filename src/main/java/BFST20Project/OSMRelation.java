package BFST20Project;

import java.util.List;

public class OSMRelation {
    long id;
    List<OSMWay> ways;

    public OSMRelation(long id) {
        this.id = id;
    }

    public void addWay(OSMWay way){
        ways.add(way);
    }
}
