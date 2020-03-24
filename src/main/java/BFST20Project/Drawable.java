package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {

    void stroke(GraphicsContext gc);
    void fill(GraphicsContext gc);

    WayType getWayType();
    void setWayType(WayType type);

}
