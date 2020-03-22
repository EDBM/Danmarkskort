package BFST20Project;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DefaultColorScheme extends ColorScheme {
    public Paint getStroke(WayType wayType) {
        switch (wayType){
            case HIGHWAY:
                return Color.BLACK;
            case BUILDING:
                return Color.BEIGE;
            case WATER:
                return Color.BLUE;
            case FOREST:
                return Color.GREEN;
            default:
                return Color.RED;
        }
    }

    public Paint getFill(WayType wayType) {
        switch (wayType){
            case BUILDING:
                return Color.BEIGE;
            case WATER:
                return Color.BLUE;
            case FOREST:
                return Color.GREEN;
            default:
                return Color.RED;
        }
    }


}
