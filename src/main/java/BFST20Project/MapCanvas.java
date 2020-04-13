package BFST20Project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.fxml.FXML;
import java.util.EnumMap;
import java.util.List;



public class MapCanvas extends Canvas {
    Model model;
    GraphicsContext gc = getGraphicsContext2D();
    ColorScheme colorScheme = new DefaultColorScheme();

    Affine trans = new Affine();


    public void init(Model model){
        this.model = model;
        model.addObserver(this::repaint);
        resetView();

    }

    public void resetView() {
        var zl = 10000;
        pan(-model.minLon, -model.minLat);

        zoom(zl, 0, 0);
        repaint();

    }

    public void repaint(){
        ZoomLevel zoomLevel = curZoomLevel();
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0,0,getWidth(),getHeight());
        gc.setTransform(trans);

        gc.setFill(Color.LIGHTGREEN);
        gc.setStroke(Color.BLACK);


        double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));
        gc.setLineWidth(pixelWidth);

        EnumMap<WayType, List<Drawable>> drawables = model.getDrawables();
        for (WayType type : drawables.keySet()){
            final var lineWidth = colorScheme.getWidth(type) * pixelWidth;
            gc.setLineWidth(lineWidth);
            if(zoomLevel.compareTo(ZoomLevel.levelForWayType(type)) >= 0){
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
        }
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
