package BFST20Project;

public class Controller {
    private Model model;
    double xCoords, yCoords;
    private View view;
    Polylines polylines;
    private MapCanvas canvas;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        view.canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY());
        });
    }
}





