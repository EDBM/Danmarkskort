package BFST20Project;

import BFST20Project.Drawable;
import BFST20Project.Point;
import BFST20Project.Routeplanner.Vertex;
import BFST20Project.WayType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class PointOfInterest implements Drawable {

    Point coordinate;
    Boolean type;


    public PointOfInterest(Point point, Boolean type){
        this.coordinate = point;
        this.type = type;
    }


    @Override
    public void stroke(GraphicsContext gc, boolean shouldFill) {
        String imageUrl;

        if(type) {imageUrl = "images/pointer.png";}
        else {imageUrl = "images/pointerPurple.png";}
        Image image = new Image(imageUrl, 0.01, 0.01, false, false);
        double size = gc.getLineWidth();
        gc.drawImage(image, coordinate.getX()-0.001485*8000*size, coordinate.getY()-0.005*8000*size, 40*size, 40*size);
    }


    @Override
    public WayType getWayType() {
        return WayType.POINT_OF_INTEREST;
    }

    @Override
    public void setWayType(WayType type) {}

    @Override
    public float getMaxX() {
        return 0;
    }

    @Override
    public float getMinX() { return 0; }

    @Override
    public float getMaxY() {
        return 0;
    }

    @Override
    public float getMinY() {
        return 0;
    }

    @Override
    public Vertex getVertex() {
        return null;
    }

    @Override
    public float distanceTo(Point point) {
        return (float) Point.distanceBetweenPoint(this.coordinate, point);
    }

    @Override
    public Collection<Point> getCoordinates() {
        return new ArrayList<Point>((Collection<? extends Point>) this.coordinate);
    }

    @Override
    public String getName() {
        return null;
    }
}
