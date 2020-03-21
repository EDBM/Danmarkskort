package BFST20Project;

import javafx.scene.paint.Paint;

public abstract class ColorScheme {
    public abstract Paint getStroke(WayType wayType);

    public abstract Paint getFill(WayType wayType);

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
