package BFST20Project;

import javafx.scene.paint.Paint;

import java.util.Map;

public abstract class ColorScheme {
    Map<WayType, Paint> typeToStroke;
    Map<WayType, Paint> typeToFill;

    public ColorScheme(){
        typeToStroke = createStroke();
        typeToFill = createFill();
    }

    protected abstract Map<WayType, Paint> createStroke();

    protected abstract Map<WayType, Paint> createFill();

    protected abstract Paint defaultColor();

    public Paint getStroke(WayType wayType){
        return typeToStroke.getOrDefault(wayType, defaultColor());
    }

    public Paint getFill(WayType wayType){
        return typeToFill.getOrDefault(wayType, getStroke(wayType));
    }

    public boolean shouldFill(WayType wayType) {
        switch (wayType){
            case ISLAND:
            case BUILDING:
            case WATER:
            case FOREST:
            case HEATH:
            case BEACH:
            case SCRUB:
            case MEADOW:
            case RESIDENTIAL:
            case WETLAND:
            case PIER:
            case BREAKWATER:
            case TOURISM:
            case LEISURE:
            case PARKING:
                return true;
            default:
                return false;
        }
    }
    public double getWidth(WayType wayType){
        switch (wayType){
            case MOTORWAY:
            case WATERWAY:
                return 2;
            case SECONDARY:
                return 1.5;
            case MINIWAY:
            case DIRTROAD:
                return 0.75;
            default:
                return 1;
        }
    }
}
