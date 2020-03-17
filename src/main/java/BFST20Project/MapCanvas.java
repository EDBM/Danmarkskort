package BFST20Project;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import java.util.ArrayList;

/*

public class MapCanvas extends Canvas {
    private GraphicsContext gc = getGraphicsContext2D();
    Affine transform = new Affine();
    private Model model;

    private ArrayList<Drawable> buildingList = new ArrayList<>();
    private ArrayList<Drawable> naturalList = new ArrayList<>();
    private ArrayList<Drawable> waterList = new ArrayList<>();
    private ArrayList<Drawable> highwayList = new ArrayList<>();
    private ArrayList<Drawable> coastlineList = new ArrayList<>();
    private ArrayList<Drawable> beachList = new ArrayList<>();
    private ArrayList<Drawable> waterwayList = new ArrayList<>();
    private ArrayList<Drawable> amenityList = new ArrayList<>();
    private ArrayList<Drawable> motorwayList = new ArrayList<>();
    private ArrayList<Drawable> miniwayList = new ArrayList<>();
    private ArrayList<Drawable> dirtroadList = new ArrayList<>();
    private ArrayList<Drawable> buswayList = new ArrayList<>();
    private ArrayList<Drawable> parkingList = new ArrayList<>();
    private ArrayList<Drawable> serviceList = new ArrayList<>();
    private ArrayList<Drawable> harbourList = new ArrayList<>();
    private ArrayList<Drawable> forestList = new ArrayList<>();
    private ArrayList<Drawable> sidewalkList = new ArrayList<>();
    private ArrayList<Drawable> leisureList = new ArrayList<>();
    private ArrayList<Drawable> subwayList = new ArrayList<>();
    private ArrayList<Drawable> bridgeList = new ArrayList<>();


    public void init(Model model) {
        this.model = model;

        model.addObserver(this::repaint);

        repaint();

    }

    public void initList() {
        for (Drawable way : model.getWaysOfType(WayType.HIGHWAY)) highwayList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.MOTORWAY)) motorwayList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.BUILDING)) buildingList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.NATURAL)) naturalList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) coastlineList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.BEACH)) beachList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.WATERWAY)) waterwayList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.AMENITY)) amenityList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.MINIWAY)) miniwayList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.DIRTROAD)) dirtroadList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.BUSWAY)) buswayList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.PARKING)) parkingList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.SERVICE)) serviceList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.HARBOUR)) harbourList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.FOREST)) forestList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.SIDEWALK)) sidewalkList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.LEISURE)) leisureList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.BRIDGE)) bridgeList.add(way);
        for (Drawable way : model.getWaysOfType(WayType.SUBWAY)) subwayList.add(way);

    }

    public void repaint() {
        gc.save();
        gc.setTransform(new Affine());


        if (model.getWaysOfType(WayType.COASTLINE).iterator().hasNext()) {
            gc.setFill(Color.web("#AADAFF"));
            gc.setStroke(Color.web("#AADAFF"));

        }

        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(transform);
        gc.setLineWidth(1 / Math.sqrt(Math.abs(transform.determinant())));
        gc.setLineDashes(5);
        gc.setLineDashOffset(2.0);


        gc.setFillRule(FillRule.EVEN_ODD);
        for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) {
            way.fill(gc);
            way.stroke(gc);
        }

        gc.setLineWidth(1 / Math.sqrt(Math.abs(transform.determinant())));
        gc.setLineCap(StrokeLineCap.ROUND);
    }
}

 */