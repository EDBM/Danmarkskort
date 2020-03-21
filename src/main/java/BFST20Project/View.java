package BFST20Project;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.nio.file.Watchable;
import java.util.EnumMap;
import java.util.List;

public class View {
    Model model;
    Stage stage;
    Canvas canvas = new Canvas(640,480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    BorderPane pane = new BorderPane(canvas);
    Scene scene = new Scene(pane);
    Affine trans = new Affine();
    ColorScheme colorScheme = new DefaultColorScheme();

    public View(Model model, Stage primaryStage){
        this.model=model;
        this.stage=primaryStage;
        model.addObserver(this::repaint);
        stage.setScene(scene);
        stage.show();
        resetView();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        canvas.widthProperty().addListener((a,b,c) -> repaint());
        canvas.heightProperty().addListener((a,b,c) -> repaint());
    }

    public void resetView() {
        pan(-model.minLon, -model.minLat);
        zoom(canvas.getWidth() / (model.maxLat - model.minLat), 0, 0);
        repaint();
    }

    public void repaint(){
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
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
                if(shouldFill)
                    drawable.fill(gc);
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
