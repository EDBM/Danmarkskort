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
        return typeToFill.getOrDefault(wayType, defaultColor());
    }

    public boolean shouldFill(WayType wayType) {
        switch (wayType){
            case BUILDING:
            case WATER:
            case FOREST:
                return true;
            default:
                return false;
        }
    }
}
