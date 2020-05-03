package BFST20Project;

import BFST20Project.Routeplanner.ShortestPath;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.EnumMap;
import java.util.HashSet;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;

public class Controller {
    private Model model;
    private View view;
    double x, y;
    private Point point;
    private Trie trie;
    private AddressParser addressParser;
    private ShortestPath shortestPath;
    private Boolean isDriving = true;

    //To get km
    private double zoomFactor = 3.4 * 2857 * 1000;

    @FXML
    private MenuItem file;

    @FXML
    private MapCanvas mapCanvas;

    @FXML
    private ComboBox start, end;

    @FXML
    private Label nearestRoad;

    @FXML
    private TextArea textarea;

    @FXML
    private Text text;



    public void init(Model model) throws Exception {
        this.model = model;
        mapCanvas.init(model);
        final double factor = mapCanvas.getWidth() / (model.maxLat - model.minLat);
        mapCanvas.pan(-model.minLon, -model.minLat);
        mapCanvas.zoom(factor, 0, 0);
        zoomText(factor);
        model.addObserver(this::updateRoutePlanner);
        start.getEditor().setOnKeyTyped(this::addressStart);
        end.getEditor().setOnKeyTyped(this::addressEnd);

        System.out.println("initial factor: " + factor);
    }

    private void updateRoutePlanner(){
        if(model.routeIsChanged){
            textarea.setText(model.routeAsText);
        }
    }


    @FXML
    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case B:
                try {
                    if(mapCanvas.getDefaultcolor() == 1 || mapCanvas.getDefaultcolor() == 3 ){
                        mapCanvas.setToColorBlindMode();
                    } else{
                        mapCanvas.setDefaultcolor();
                    }
                    mapCanvas.repaint();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case L:
                try {
                    if(mapCanvas.getDefaultcolor() == 1 || mapCanvas.getDefaultcolor() == 2 ){
                        mapCanvas.setToBatmanMode();
                    } else{
                        mapCanvas.setDefaultcolor();
                    }
                    mapCanvas.repaint();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }



    @FXML
    public void chooseCarPicker(){
        model.setDriving(true);
    }

    @FXML
    public void chooseWalkPicker(){
        model.setDriving(false);
    }



    @FXML
    private void onScroll(ScrollEvent e) {
        double factor = Math.pow(1.001, e.getDeltaY());
        mapCanvas.zoom(factor, e.getX(), e.getY());
        zoomText(factor);

    }

    private String toMetric(double number){
        String[] prefix = {"","k", "M"};
        int scale = 0;
        while(number >= 1000 && scale < prefix.length -1){
            number/=1000;
            scale++;
        }
        return String.format("%.0f %s", number, prefix[scale]);
    }

    //Shorthand assigntment x /= y -> x = x / y
    private void zoomText(double delta){
        zoomFactor /= delta ;
        text.setText(toMetric(zoomFactor) + "m");
    }

    @FXML
    private void zoomOut() {
        double factor = 0.77;
        mapCanvas.zoom(factor, 600, 300);
        mapCanvas.repaint();
        zoomText(factor);
    }


    @FXML
    private void zoomIn() {
        double factor = 1.29;
        mapCanvas.zoom(1.29,600,300);
        mapCanvas.repaint();

        zoomText(factor);


    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        if (e.isPrimaryButtonDown()) mapCanvas.pan(e.getX() - x, e.getY() - y);
        x = e.getX();
        y = e.getY();
    }




    @FXML
    private void onMousePressed(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY) {
            if(e.isControlDown()) {
                System.out.println("ctrl");
                model.setNavigateFrom(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
            }
            else if(e.isShiftDown())
                model.setNavigateTo(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
        } else {
            PointOfInterest POI = new PointOfInterest(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()), true);
            model.addPointOfInterest(POI);
        }

        x = e.getX();
        y = e.getY();

        /*if(e.getButton() == MouseButton.PRIMARY){
            if(e.isControlDown()) {
                mapCanvas.highlightNearestRoad(e.getX(), e.getY());
            }*/



        mapCanvas.repaint();

    }


    @FXML
    private void onMouseMoved(MouseEvent e) {
        Point mapPoint = mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY());
        HashSet<ZoomLevel> set = new HashSet<>();
        set.add(mapCanvas.curZoomLevel());

        Drawable nearestRoad = model.nearestRoad(mapPoint, set);
        if(nearestRoad != null) {
            if (nearestRoad.getName() != null) {
                if (mapCanvas.getDefaultcolor() == 1 || mapCanvas.getDefaultcolor() == 2) {
                    this.nearestRoad.setText("Vejnavn: " + nearestRoad.getName());
                } else {
                    this.nearestRoad.setText("Vejnavn: " + nearestRoad.getName() + "-Gotham");
                }
            }
        }
    }

    private void updateComboBox(ComboBox comboBox){
        String prefix =  comboBox.getEditor().getText();
        model.getTrie().autocomplete(prefix);

        comboBox.getItems().clear();

        comboBox.getItems().addAll(model.getTrie().autocomplete(prefix));


    }

    @FXML
    private void addressStart(Event e){
        model.navigateFromAddress(start.getEditor().getText());

        //TODO implement trie for
        //List l = (List) trie.autocomplete(start.getText());

        //trie.autocomplete(start.getText());
        updateComboBox(start);




    }

    @FXML
    private void addressEnd(Event e){
        model.navigateToAddress(end.getEditor().getText());

        //TODO implement trie for
        //List l = (List) trie.autocomplete(start.getText());

        //trie.autocomplete(start.getText());
        updateComboBox(end);

    }

    @FXML
    private void fileChooser() throws Exception {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        model.loadModel(selectedFile);

        mapCanvas.resetTrans();
        zoomFactor = 3.4 * 2857 * 1000;


        double factor = mapCanvas.getWidth() / (model.maxLat - model.minLat);
        mapCanvas.pan(-model.minLon, -model.minLat);
        mapCanvas.zoom(factor, 0, 0);
        zoomText(factor);
        System.out.println("factor: " + factor);

        mapCanvas.repaint();
        System.out.println(selectedFile.getAbsolutePath());
    }

    @FXML
    private void changeColor(){
        if(mapCanvas.getDefaultcolor() == 1|| mapCanvas.getDefaultcolor() == 3){
            mapCanvas.setToColorBlindMode();
        } else{
            mapCanvas.setDefaultcolor();
        }
        mapCanvas.repaint();
    }

    @FXML
    private void changeColor2(){
        if(mapCanvas.getDefaultcolor() == 1 || mapCanvas.getDefaultcolor() == 2){
            mapCanvas.setToBatmanMode();
        } else{
            mapCanvas.setDefaultcolor();
        }
        mapCanvas.repaint();
    }
}




















