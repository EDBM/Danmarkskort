package BFST20Project;

import BFST20Project.Routeplanner.Vertex;
import javafx.scene.canvas.GraphicsContext;

public interface Drawable {

    void stroke(GraphicsContext gc, boolean shouldFill);

    WayType getWayType();
    void setWayType(WayType type);

    float getMaxX();
    float getMinX();
    float getMaxY();
    float getMinY();

    Vertex getVertex();

    float distanceTo(Point point);

    String getName();

}
