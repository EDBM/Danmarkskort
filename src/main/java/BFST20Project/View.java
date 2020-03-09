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
    OSMParser osmParser;

    public View(Model model, Stage primaryStage) throws FileNotFoundException, XMLStreamException {
        this.model=model;
        this.stage=primaryStage;
        model.addObserver(this::repaint);
        stage.setScene(scene);
        stage.show();
        resetView();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        canvas.widthProperty().addListener((a,b,c) -> {
            repaint();
        });
        canvas.heightProperty().addListener((a,b,c) -> {
            repaint();
        });
    }

    public void resetView() {
        pan(-osmParser.minlon, -osmParser.minlat);
        zoom(canvas.getWidth() / (osmParser.maxlat - osmParser.minlat), 0, 0);
        repaint();
    }

    public void repaint(){
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setTransform(trans);
        gc.setFill(Color.LIGHTGREEN);
        for (Drawable drawable : osmParser){
            drawable.stroke(gc);
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
