package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.nio.channels.NotYetBoundException;
import java.util.ArrayList;

public class PolylinesRelation extends ArrayList<Polylines> implements Drawable, Serializable {

    /*public PolylinesRelation(OSMWaysRelations rel) {
        for (OSMWay way : rel)
            add(new Polylines(way));
    }*/

    @Override
    public void stroke(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }

    public void trace(GraphicsContext gc) {
        for (Polylines p : this) p.trace(gc);
    }


    public void fill(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.fill();
    }

    @Override
    public WayType getWayType() {
        return null;
    }

    @Override
    public void setWayType(WayType type) { throw new NotYetBoundException(); }

}
