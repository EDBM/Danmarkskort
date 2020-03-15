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

public class View {
    Model model;
    Stage stage;
    Canvas canvas = new Canvas(640,480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    BorderPane pane = new BorderPane(canvas);
    Scene scene = new Scene(pane);
    Affine trans = new Affine();
    MapCanvas mapCanvas = new MapCanvas();

    public View(Model model, Stage primaryStage){
        this.model=model;
        this.stage=primaryStage;
        model.addObserver(this::mapCanvas.repaint);
        stage.setScene(scene);
        stage.show();
        resetView();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        canvas.widthProperty().addListener((a,b,c) -> mapCanvas.repaint());
        canvas.heightProperty().addListener((a,b,c) -> mapCanvas.repaint());
    }

    public void resetView() {
        pan(-model.minLon, -model.minLat);
        zoom(canvas.getWidth() / (model.maxLat - model.minLat), 0, 0);
        mapCanvas.repaint();
    }


    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        mapCanvas.repaint();
    }
    public void zoom(double factor, double x, double y) {
        trans.prependScale(factor, factor, x, y);
        mapCanvas.repaint();
    }
}
