package BFST20Project;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MapCanvas extends Canvas {
    Model model;
    GraphicsContext gc = getGraphicsContext2D();
    ColorScheme colorScheme = new DefaultColorScheme();
    Affine trans = new Affine();

    public MapCanvas(int width, int height) {
        super(width, height);
    }

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
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0,0,getWidth(),getHeight());
        gc.setTransform(trans);
        gc.setFill(Color.LIGHTGREEN);
        gc.setStroke(Color.BLACK);

        Point topLeft, bottomRight;

        try {
            topLeft = new Point(trans.inverseTransform(0, 0));
            bottomRight = new Point(trans.inverseTransform(getWidth(), getHeight()));
        } catch (NonInvertibleTransformException e) {
            topLeft = new Point(model.minLat, model.minLon);
            bottomRight = new Point(model.maxLon, model.maxLat);
            System.out.println("oops");
        }

        double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));
        // gc.setLineWidth(pixelWidth);

        Map<WayType, List<Drawable>> drawables = model.getDrawables(curZoomLevel(), topLeft, bottomRight);

        for (WayType type : drawables.keySet()){
            gc.setLineWidth(colorScheme.getWidth(type) * pixelWidth);
            gc.setStroke(colorScheme.getStroke(type));
            boolean shouldFill = colorScheme.shouldFill(type);
            if(shouldFill){
                gc.setFill(colorScheme.getFill(type));
            }
            for(Drawable drawable : drawables.get(type)){
                drawable.stroke(gc);
                if(shouldFill) {
                    drawable.fill(gc);
                }
            }
        }
        //drawRectangle(topLeft, bottomRight, pixelWidth * 3);
    }

    private void drawRectangle(Point2D p1, Point2D p2, double width){
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(width);

        gc.moveTo(p1.getX(), p1.getY());
        gc.lineTo(p1.getX(), p2.getY());
        gc.lineTo(p2.getX(), p2.getY());
        gc.lineTo(p2.getX(), p1.getY());
        gc.lineTo(p1.getX(), p1.getY());
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
}
