package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class OSMRelation implements Drawable {
    long id;
    List<OSMWay> ways = new ArrayList<>();

    public OSMRelation(long id) {
        this.id = id;
    }

    public void addWay(OSMWay way){
        ways.add(way);
    }

    public void addAllWays(ArrayList<OSMWay> osmWays) { ways.addAll(osmWays); }

    @Override
    public void stroke(GraphicsContext gc) {

    }

    @Override
    public void fill(GraphicsContext gc) {

    }
}