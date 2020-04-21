package BFST20Project;
import javafx.geometry.Point2D;
import com.sun.javafx.scene.control.inputmap.InputMap;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.control.ScrollToEvent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {

    private Model model;
    double xCoords, yCoords;
    private View view;
    double x, y;
    ImageView imageView = new ImageView();


    private static final String IMAGE1 = "\\resources\\images\\PoI.png";
    @FXML
    private MapCanvas mapCanvas;
    @FXML
    private TextField start, slut;




    public void init(Model model) throws Exception {
        this.model = model;
        mapCanvas.init(model);


    }


    @FXML
    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case V:
                try {
                    new View(model, new Stage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }



    @FXML
    public void chooseCarPicker(){
        //TODO shortest path for car
        System.out.println("her må jeg køre");
    }

    @FXML
    public void chooseWalkPicker(){
        //TODO shortest path for walking
        System.out.println("her må jeg gå");
    }

        @FXML
        private void onScroll(ScrollEvent e) {
            double factor = Math.pow(1.001, e.getDeltaY());
            mapCanvas.zoom(factor, e.getX(), e.getY());
            System.out.println(e.getDeltaY());
            System.out.println(x + y);
        }



    @FXML
    private void zoomOut() {
        mapCanvas.zoom(0.77, 600, 300);
        mapCanvas.repaint();
    }

    @FXML
    private void zoomIn() {

        mapCanvas.zoom(1.29,600,300);
        mapCanvas.repaint();

    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        if (e.isPrimaryButtonDown()) mapCanvas.pan(e.getX() - x, e.getY() - y);
        x = e.getX();
        y = e.getY();
    }




    @FXML
    private void onMousePressed(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
        x = e.getX();
        y = e.getY();
        System.out.println(x);
        }else {
            System.out.println("Nu højreklikker jeg");
            x = e.getX();
            y = e.getY();
            }

        }


        @FXML
            private void addressStart(KeyEvent e){
            model.getTrie().autocomplete(e.toString());
            //TODO implement trie for
            System.out.println("her skal der være en adresse :)");
        }

        @FXML
        private void addressSlut(KeyEvent e){

            //TODO implement trie
            System.out.println("her skal der være en adresse :)");
        }


    }






