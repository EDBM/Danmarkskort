package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.List;


public interface Drawable {

    void stroke(GraphicsContext gc);
    void fill(GraphicsContext gc);

    WayType getWayType();
    void setWayType(WayType type);

    Collection<Point> getCoordinates();

}
