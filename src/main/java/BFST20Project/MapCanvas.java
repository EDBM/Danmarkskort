package BFST20Project;

import BFST20Project.ColourSchemes.BarbieScheme;
import BFST20Project.ColourSchemes.BatManScheme;
import BFST20Project.ColourSchemes.ColorScheme;
import BFST20Project.ColourSchemes.DefaultColorScheme;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.*;

public class MapCanvas extends Canvas{
    private Model model;

    private GraphicsContext gc = getGraphicsContext2D();
    private ColorScheme colorScheme = new DefaultColorScheme();
    private ColorScheme colorBarbieScheme = new BarbieScheme();
    private ColorScheme colorBatmanScheme = new BatManScheme();
    private Affine trans = new Affine();
    private int defaultColor = 1;

    public void init(Model model){
        this.model = model;
        model.addObserver(this::repaint);
        repaint();
    }

    public void resetTrans(){
        this.trans = new Affine();
    }

    public void repaint(){
        gc.setTransform(new Affine());

        if(defaultColor == 2){
            gc.setFill(Color.web("#F6CEF5"));

        }
        else if(defaultColor == 3){
            gc.setFill(Color.web("#6C6868"));

        }
        else{
            gc.setFill(Color.LIGHTBLUE);
        }
        gc.fillRect(0,0,getWidth(),getHeight());
        gc.setTransform(trans);

        Point topLeft, bottomRight;

        try {
            topLeft = new Point(trans.inverseTransform(0, 0));
            bottomRight = new Point(trans.inverseTransform(getWidth(), getHeight()));
        } catch (NonInvertibleTransformException e) {
            // This cannot happen
            topLeft = new Point(model.minLat, model.minLon);
            bottomRight = new Point(model.maxLon, model.maxLat);
        }

        double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));

        Map<WayType, List<Drawable>> drawables = model.getDrawables(curZoomLevel(), topLeft, bottomRight);
        for (WayType type : drawables.keySet()){
            var color = colorScheme;

            if(defaultColor == 2){
                color = colorBarbieScheme;
            }
            if(defaultColor == 3){
                color = colorBatmanScheme;
            }

            gc.setLineWidth(color.getWidth(type) * pixelWidth);
            gc.setStroke(color.getStroke(type));
            boolean shouldFill = color.shouldFill(type);
            if (shouldFill) {
                gc.setFill(color.getFill(type));
            }

            for(Drawable drawable : drawables.get(type)){
                drawable.stroke(gc, shouldFill);
            }
        }
    }

    public void setToBatmanMode(){
        defaultColor = 3;
    }

    public void setToBarbieMode(){
        defaultColor = 2;
    }

    public void setDefaultColor(){
        defaultColor = 1;
    }

    public int getDefaultColor(){
        return defaultColor;
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
