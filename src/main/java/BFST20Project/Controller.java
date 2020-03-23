package BFST20Project;
import javafx.geometry.Point2D;

public class Controller {
    private Model model;
    double xCoords, yCoords;
    private View view;
    Polylines polylines;
    Point2D lastmouse;
    Polylines dragged;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
       /* view.canvas.setOnMousePressed(e -> {
                Point2D mc = view.pointCoordinates(e.getX(), e.getY());
         });
        view.canvas.setOnMouseDragged(e ->{
            if(dragged==null){
                view.pan(e.getX() - lastmouse.getX(), e.getY() - lastmouse.getY());
                lastmouse = new Point2D(e.getX(),e.getY());
            } else {
                Point2D mc = view.pointCoordinates(e.getX(),e.getY());
            }
        });*/

        view.canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.canvas.zoom(factor, e.getX(), e.getY());
        });

    }

}





