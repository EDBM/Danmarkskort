package BFST20Project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.EnumMap;
import java.util.List;

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

        double pixelWidth = 1/Math.sqrt(Math.abs(trans.determinant()));
        gc.setLineWidth(pixelWidth);

        EnumMap<WayType, List<Drawable>> drawables = model.getDrawables();
        for (WayType type : drawables.keySet()){
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

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
    }
    public void zoom(double factor, double x, double y) {
        trans.prependScale(factor, factor, x, y);
        repaint();
    }
}
