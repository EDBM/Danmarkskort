package BFST20Project;

import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.util.HashSet;
import java.io.File;

public class Controller {
    private Model model;
    double x, y;

    //To get km
    private double zoomFactor = 3.4 * 2857 * 1000;

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



    public void init(Model model){
        this.model = model;
        mapCanvas.init(model);
        final double factor = mapCanvas.getWidth() / (model.maxLat - model.minLat);
        mapCanvas.pan(-model.minLon, -model.minLat);
        mapCanvas.zoom(factor, 0, 0);
        zoomText(factor);
        model.addObserver(this::updateRoutePlanner);
        start.getEditor().setOnKeyTyped(this::addressStart);
        end.getEditor().setOnKeyTyped(this::addressEnd);
    }

    @FXML
    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case B:
                try {
                    if(mapCanvas.getDefaultColor() == 1 || mapCanvas.getDefaultColor() == 3 ){
                        mapCanvas.setToBarbieMode();
                    } else{
                        mapCanvas.setDefaultColor();
                    }
                    mapCanvas.repaint();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case L:
                try {
                    if(mapCanvas.getDefaultColor() == 1 || mapCanvas.getDefaultColor() == 2 ){
                        mapCanvas.setToBatmanMode();
                    } else{
                        mapCanvas.setDefaultColor();
                    }
                    mapCanvas.repaint();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
        }
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
            return String.format("%.1f %s", number, prefix[scale]);
        }
        return String.format("%.0f %s", number, prefix[scale]);
    }

    private void zoomText(double delta){
        zoomFactor /= delta ;
        text.setText(toMetric(zoomFactor) + "m");
    }

    @FXML
    private void zoomOut() {
        double factor = 0.77;
        mapCanvas.zoom(factor, 600, 300);
        zoomText(factor);
    }

    @FXML
    private void zoomIn() {
        double factor = 1.29;
        mapCanvas.zoom(factor,600,300);
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
            if (e.isControlDown()) {
                model.setNavigateFrom(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
            } else if (e.isShiftDown()) {
                model.setNavigateTo(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
            }
        } else {
            PointOfInterest POI = new PointOfInterest(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()), true);
            model.addPointOfInterest(POI);
        }

        x = e.getX();
        y = e.getY();

        mapCanvas.repaint();
    }


    @FXML
    private void onMouseMoved(MouseEvent e) {
        Point mapPoint = mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY());
        HashSet<ZoomLevel> set = new HashSet<>();
        set.add(mapCanvas.curZoomLevel());
        Drawable nearestRoad = model.nearestRoad(mapPoint, set);

        if(nearestRoad != null && nearestRoad.getName() != null) {
            if (mapCanvas.getDefaultColor() == 1 || mapCanvas.getDefaultColor() == 2) {
                this.nearestRoad.setText("Vejnavn: " + nearestRoad.getName());
            } else {
                this.nearestRoad.setText("Vejnavn: " + nearestRoad.getName() + "-Gotham");
            }
        }
    }

    private void updateComboBox(ComboBox comboBox){
        String prefix =  comboBox.getEditor().getText();
        model.getTrie().autocomplete(prefix);

        comboBox.getItems().clear();

        comboBox.getItems().addAll(model.getTrie().autocomplete(prefix));
    }

    private void updateRoutePlanner(){
        if(model.routeIsChanged){
            textarea.setText(model.routeAsText);
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
    private void addressStart(Event e){
        model.navigateFromAddress(start.getEditor().getText());
        updateComboBox(start);
    }

    @FXML
    private void addressEnd(Event e){
        model.navigateToAddress(end.getEditor().getText());
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
        zoomText(factor);
        mapCanvas.zoom(factor, 0, 0);
    }

    @FXML
    private void changeToBarbieMode(){
        if(mapCanvas.getDefaultColor() == 2){
            mapCanvas.setDefaultColor();
        } else{
            mapCanvas.setToBarbieMode();
        }
        mapCanvas.repaint();
    }

    @FXML
    private void changeToBatmanMode(){
        if(mapCanvas.getDefaultColor() == 3){
            mapCanvas.setDefaultColor();
        } else{
            mapCanvas.setToBatmanMode();
        }
        mapCanvas.repaint();
    }
}