package BFST20Project;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.*;

import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class Controller {

    private Model model;
    private View view;
    double x, y;
    private Point point;





    @FXML private AnchorPane root;

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
            case B:
                try {
                    if(mapCanvas.getDefaultcolor() == 1){
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
                    if(mapCanvas.getDefaultcolor() == 3){
                        mapCanvas.setToColorBlindMode();
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
        if(e.getButton() == MouseButton.PRIMARY) {
            if(e.isControlDown()) {
                System.out.println("ctrl");
                model.setNavigateFrom(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
            }
            else if(e.isShiftDown())
                model.setNavigateTo(mapCanvas.screenCoordinatesToPoint(e.getX(), e.getY()));
        }

        x = e.getX();
        y = e.getY();

        if(e.getButton() == MouseButton.PRIMARY){
            System.out.println(x);


        }else {
            mapCanvas.highlightNearestRoad(e.getX(), e.getY());

        }

            }


    @FXML
     private void addressStart(KeyEvent e){
         //TODO implement trie for
         System.out.println("her skal der være en adresse :)");
        }

     @FXML
     private void addressSlut(KeyEvent e){

            //TODO implement trie
        System.out.println("her skal der være en adresse :)");
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






