package BFST20Project;

import BFST20Project.Routeplanner.Vertex;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.List;


public interface Drawable {

    void stroke(GraphicsContext gc);
    void fill(GraphicsContext gc);

    WayType getWayType();
    void setWayType(WayType type);

    float getMaxX();
    float getMinX();
    float getMaxY();
    float getMinY();

    Vertex getVertex();

    float distanceTo(Point point);

    Collection<Point> getCoordinates();

}
