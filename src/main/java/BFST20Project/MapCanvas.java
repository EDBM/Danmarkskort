package BFST20Project;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.image.Image;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;





public class MapCanvas extends Canvas {
    Model model;

    final Image image = new Image("file:resources/images/pointer.png");
    GraphicsContext gc = getGraphicsContext2D();
    ColorScheme colorScheme = new DefaultColorScheme();
    ColorScheme colorBlindScheme = new ColorBlindScheme();
    ColorScheme colorBatmanScheme = new BatManScheme();
    Affine trans = new Affine();
    int defaultcolor = 1;


    public void init(Model model){
        this.model = model;
        model.addObserver(this::repaint);
        resetView();
    }



    public void resetView() {
        pan(-model.minLon, -model.minLat);
        zoom(getWidth() / (model.maxLat - model.minLat), 0, 0);
        repaint();
    }

    public void repaint(){
        gc.setTransform(new Affine());

        if(defaultcolor == 2){
            gc.fillRect(0,0,getWidth(),getHeight());
            gc.setFill(Color.RED);
            gc.setStroke(Color.DARKRED);
            System.out.println("jeg er her");
        }
        if(defaultcolor == 3){
            gc.setFill(Color.web("#6C6868"));
            gc.fillRect(0,0,getWidth(),getHeight());
            gc.setFill(Color.web("#393838"));
            gc.setStroke(Color.BLACK);

        }
        else{
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(0,0,getWidth(),getHeight());
            gc.setFill(Color.LIGHTGREEN);
            gc.setStroke(Color.BLACK);

        }
        gc.setTransform(trans);

        Point topLeft, bottomRight;

        try {
            topLeft = new Point(trans.inverseTransform(0, 0));
            bottomRight = new Point(trans.inverseTransform(getWidth(), getHeight()));
        } catch (NonInvertibleTransformException e) {
            // This cannot happen
            topLeft = new Point(model.minLat, model.minLon);
            bottomRight = new Point(model.maxLon, model.maxLat);
            System.out.println("oops");
        }

        double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));
        // gc.setLineWidth(pixelWidth);

        Map<WayType, List<Drawable>> drawables = model.getDrawables(curZoomLevel(), topLeft, bottomRight);
        for (WayType type : drawables.keySet()){
            var color = colorScheme;

            if(defaultcolor == 2){
                color = colorBlindScheme;
            }
            if(defaultcolor == 3){
                color = colorBatmanScheme;
            }

            gc.setLineWidth(color.getWidth(type) * pixelWidth);
            gc.setStroke(color.getStroke(type));
            boolean shouldFill = color.shouldFill(type);
            if (shouldFill) {
                gc.setFill(color.getFill(type));

            }

            for(Drawable drawable : drawables.get(type)){
                drawable.stroke(gc);
                if(shouldFill) {
                    drawable.fill(gc);
                }
            }

        }

    }

    public void setToBatmanMode(){
        defaultcolor=3;
    }

    public void setToColorBlindMode(){
        defaultcolor=2;
    }

    public void setDefaultcolor(){
        defaultcolor =1;
    }


    public int getDefaultcolor(){
        return defaultcolor;
    }

    public ZoomLevel curZoomLevel(){
        double scale = trans.getMxx(); // scaling of the x-axis. Should be the same as the y-axis
        if(scale > 20000)
            return ZoomLevel.LEVEL_3;
        if(scale > 10000)
            return ZoomLevel.LEVEL_2;
        return ZoomLevel.LEVEL_1;
    }

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
    }
    public void zoom(double factor, double x, double y) {
        trans.prependScale(factor, factor, x, y);
        repaint();
    }


    public void highlightNearestRoad(double x, double y) {
            Point mapPoint = screenCoordinatesToPoint(x, y);
            Drawable nearestRoad = model.nearestRoad(mapPoint);
            double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));
            gc.setLineWidth(3 * pixelWidth);
            gc.setStroke(Color.PURPLE);
            nearestRoad.stroke(gc);


    }

    public Point screenCoordinatesToPoint(double x, double y){
        try {
            return new Point(trans.inverseTransform(x, y));
        } catch (NonInvertibleTransformException e){
            // This cannot happen with the transforms we are using
            e.printStackTrace();
            return null;
        }
    }


}
